package org.rubicon.game.impl.events

import org.rubicon.game.IEvent
import kotlin.js.JsExport

@JsExport
open class Event<T: Enum<T>, S>(
    override val type: T,
    override val source: S
) : IEvent<T, S>