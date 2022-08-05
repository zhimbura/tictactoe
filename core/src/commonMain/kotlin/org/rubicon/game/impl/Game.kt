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

    // Содержит информацию о том кто в данный момент времени ходит
    private var currentPlayer: PlayerType = PlayerType.CROSS

    // Все возможные выигрышные позиции
    private val winConditions: List<List<FieldCell>> by lazy {
        this.getAllWinConditions()
    }

    /**
     * Обработчик события клика по ICell
     *
     * Проставляет ячейки кликнувшего игрока, переключает ход игрока и проверяет завершенность игры
     *
     * */
    private fun onClickFieldCell(event: IEvent<FieldCellEvents, FieldCell>) {
        if (this.currentPlayer != PlayerType.NONE) {
            val fieldCell: FieldCell = event.source
            this.changeFiledCellState(fieldCell, currentPlayer)
            this.turnMove()
            this.checkGameOver()
        }
    }

    /**
     * Меняет состояние ячейки и сообщает об этом событием
     * */
    private fun changeFiledCellState(fieldCell: FieldCell, state: PlayerType) {
        fieldCell.changeState(state)
        this.emit(GameCellEvent(this, fieldCell))
    }

    /**
     * Переключает текущего игрока на его противника или на сторону по умолчанию
     * */
    private fun turnMove() {
        this.currentPlayer = when (currentPlayer) {
            PlayerType.CROSS -> PlayerType.ZERO
            PlayerType.ZERO,
            PlayerType.NONE -> PlayerType.CROSS
        }
    }

    /**
     * Проверяет статус окончания игры
     * */
    private fun checkGameOver() {
        val winLine = this.winConditions.find { this.isMatchLine(it) }
        val event = when {
            winLine != null -> GameOverEvent(this, winLine)
            !hasFreeCells() -> GameOverEvent(this)
            else -> null
        }
        if (event != null) {
            this.currentPlayer = PlayerType.NONE
            this.emit(event)
        }
    }

    /**
     * Проверяет наличие ячеек на которые еще не кликали
     * */
    private fun hasFreeCells(): Boolean {
        return this.field.any { row ->
            row.any { it.getState() == PlayerType.NONE }
        }
    }

    /**
     * Проверяет что переданная комбинация ячеек выбрана одним игроком
     * */
    private fun isMatchLine(row: List<FieldCell>): Boolean {
        if (row.isEmpty()) return false
        val firstState = row.first().getState()
        if (firstState == PlayerType.NONE) return false
        return row.all { it.getState() == firstState }
    }

    /**
     * Возвращает все возможные выигрышные комбинации
     * */
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

    /**
     * Сбрасывает игру чтобы представление получило обновление состояний и отрисовало поле
     * */
    override fun play() {
        // Суть метода заключается в том чтобы отрисовать ячейки игрового поля
        // Поэтому reset сюда подходит
        this.reset()
    }

    /**
     * Сбрасывает состояние ячеек на состояние ни кем не выбрано
     * */
    override fun reset() {
        this.currentPlayer = PlayerType.CROSS
        for (y in 0 until this.field.size) {
            for (x in 0 until this.field[y].size) {
                this.changeFiledCellState(this.field[y][x], PlayerType.NONE)
            }
        }
    }

}