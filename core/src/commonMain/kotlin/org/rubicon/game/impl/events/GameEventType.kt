package org.rubicon.game.impl.events

import kotlin.js.JsExport

/**
 * События, которые могут случиться во время игры
 * */
@JsExport
enum class GameEventType {
    /**
     * Игра окончена
     * */
    GAME_OVER,
    /**
     * Изменилось состояние поля
     * */
    CHANGE_CELL
}