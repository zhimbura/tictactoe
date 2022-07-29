package org.rubicon.game.impl.events

import org.rubicon.game.DuplicateListenersException
import org.rubicon.game.EventCallBack
import org.rubicon.game.IEvent
import org.rubicon.game.IEventEmitter
import kotlin.js.JsExport

@JsExport
abstract class EventEmitter<T : Enum<T>, S> : IEventEmitter<T, S> {
    private val listeners: LinkedHashMap<T, ArrayList<EventCallBack<T, S>>> = linkedMapOf()

    override fun on(eventType: T, callBack: EventCallBack<T, S>) {
        val eventTypeCollection = listeners.getOrPut(eventType) {
            arrayListOf()
        }
        if (callBack in eventTypeCollection) {
            throw DuplicateListenersException("Данная подписка уже существует.")
        }
        eventTypeCollection.add(callBack)
    }

    override fun del(eventType: T, callBack: EventCallBack<T, S>) {
        listeners[eventType]?.remove(callBack)
    }

    override fun once(eventType: T, callBack: EventCallBack<T, S>) {
        this.on(eventType) {
            this.del(eventType, callBack)
            callBack(it)
        }
    }

    protected fun emit(event: IEvent<T, S>) {
        listeners[event.type]?.forEach {
            it(event)
        }
    }
}