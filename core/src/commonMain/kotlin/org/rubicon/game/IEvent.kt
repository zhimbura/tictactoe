package org.rubicon.game

import kotlin.js.JsExport

/**
 * Событие, которое получит пользователь при срабатывании подписки
 * @property type Тип события
 * @property source Источник события
 */
@JsExport
interface IEvent<T : Enum<T>, S> {
    val type: T
    val source: S

    /**
     * Данный метод при деструктуризации вернет тип события
     * */
    operator fun component1(): T = this.type

    /**
     * Данный метод при деструктуризации вернет источник события
     * */
    operator fun component2(): S = this.source
}