package org.rubicon.game

import org.rubicon.game.impl.events.GameEventType
import kotlin.js.JsExport

/**
 * Игра
 * Реализует главную логику игры кидает события по окончанию игры или при изменении состояния игрового поля
 * */
@JsExport
interface IGame : IEventEmitter<GameEventType, IGame> {
    /**
     * Запускает игру
     *
     * Кидает события об изменении каждой игровой ячейки для отрисовки поля
     * */
    fun play()

    /**
     * Сбрасывает состояние игры
     * */
    fun reset()
}