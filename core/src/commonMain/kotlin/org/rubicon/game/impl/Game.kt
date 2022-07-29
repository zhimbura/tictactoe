package org.rubicon.game.impl

import org.rubicon.game.IEvent
import org.rubicon.game.IGame
import org.rubicon.game.impl.events.EventEmitter
import org.rubicon.game.impl.events.GameCellEvent
import org.rubicon.game.impl.events.GameEventType
import org.rubicon.game.impl.events.GameOverEvent
import kotlin.js.JsExport

@JsExport
class Game : EventEmitter<GameEventType, IGame>(), IGame {
    // Сразу инициализируем поле 3 x 3 и подписываемся на событие клика
    private val field: List<List<FieldCell>> = List(3) { y ->
        List(3) { x ->
            FieldCell(x, y).also {
                it.on(FieldCellEvents.CLICK, this::onClickFieldCell)
            }
        }
    }

    // Первым ходят крестики
    private var playerType: PlayerType = PlayerType.CROSS
    private val winConditions: List<List<FieldCell>> by lazy {
        this.getAllWinConditions()
    }

    private fun onClickFieldCell(event: IEvent<FieldCellEvents, FieldCell>) {
        val fieldCell: FieldCell = event.source
        this.changeFiledCellState(fieldCell, playerType)
        this.turnMove()
        this.checkGameOver()

    }

    private fun changeFiledCellState(fieldCell: FieldCell, state: PlayerType) {
        fieldCell.changeState(playerType)
        this.emit(GameCellEvent(this, fieldCell))
    }

    private fun turnMove() {
        this.playerType = when (playerType) {
            PlayerType.CROSS -> PlayerType.ZERO
            PlayerType.ZERO,
            PlayerType.NONE -> PlayerType.CROSS
        }
    }

    private fun checkGameOver() {
        val winLine = this.winConditions.find { this.equalsRow(it) }
        when {
            winLine != null -> this.emit(GameOverEvent(this, winLine))
            !hasFreeCells() -> this.emit(GameOverEvent(this))
        }
    }

    private fun hasFreeCells(): Boolean {
        return this.field.any { row ->
            row.any { it.getState() == PlayerType.NONE }
        }
    }

    private fun equalsRow(row: List<FieldCell>): Boolean {
        if (row.isEmpty()) return false
        val firstState = row.first().getState()
        return row.all { it.getState() == firstState }
    }

    private fun getAllWinConditions(): List<List<FieldCell>> {
        val result: ArrayList<List<FieldCell>> = arrayListOf()
        val diagonal: ArrayList<FieldCell> = arrayListOf()
        val diagonalOpposite: ArrayList<FieldCell> = arrayListOf()
        for (i in 0..2) {
            // Добавляем все строчки
            result.add(
                listOf(
                    this.field[i][0],
                    this.field[i][1],
                    this.field[i][2],
                )
            )
            // Добавляем все колонки
            result.add(
                listOf(
                    this.field[0][i],
                    this.field[1][i],
                    this.field[2][i],
                )
            )
            diagonal.add(this.field[i][i])
            diagonalOpposite.add(this.field[2 - i][i])
        }
        result.add(diagonal)
        result.add(diagonalOpposite)
        return result
    }

    override fun play() {
        // Суть метода заключается в том чтобы отрисовать ячейки игрового поля
        // Поэтому reset сюда подходит
        this.reset()
    }

    override fun reset() {
        for (y in 0 until this.field.size) {
            for (x in 0 until this.field[y].size) {
                this.changeFiledCellState(this.field[y][x], PlayerType.NONE)
            }
        }
    }

}