package org.rubicon.game.impl

import org.rubicon.game.IFieldCell
import org.rubicon.game.impl.events.Event
import org.rubicon.game.impl.events.EventEmitter
import kotlin.js.JsExport

@JsExport
enum class FieldCellEvents {
    CLICK
}

@JsExport
class FieldCell(
    private val x: Int,
    private val y: Int,
    private var state: PlayerType = PlayerType.NONE
) : EventEmitter<FieldCellEvents, FieldCell>(), IFieldCell {

    /**
     * Во время игры состояние может быть изменено либо сброшено
     * */
    internal fun changeState(state: PlayerType) {
        this.state = state
    }

    override fun click() {
        // Мы кинем событие, что пользователь кликнул
        this.emit(Event(FieldCellEvents.CLICK, this))
    }

    override fun getState(): PlayerType = this.state

    override fun getX(): Int = this.x
    override fun getY(): Int = this.y
}