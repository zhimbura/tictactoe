package org.rubicon.game

import kotlin.js.JsExport


@JsExport
class DuplicateListenersException(override val message: String) : Throwable(message)

/**
 * Тип подписки
 * данная функция обратного вызова должна принимать на вход событие
 * с которым она и будет работать
 * */
typealias EventCallBack<T, S> = (IEvent<T, S>) -> Unit

/**
 * Создает возможность подписываться на события объекта
 * */
@JsExport
interface IEventEmitter<T : Enum<T>, S> {
    /**
     * Создать подписку
     * */
    @Throws(DuplicateListenersException::class)
    fun on(eventType: T, callBack: EventCallBack<T, S>)

    /**
     * Удалить подписку
     * */
    fun del(eventType: T, callBack: EventCallBack<T, S>)

    /**
     * Создать подписку, которая будет удалена после срабатывания события
     * */
    fun once(eventType: T, callBack: EventCallBack<T, S>)
}