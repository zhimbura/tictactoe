package org.rubicon.game.impl.events

import org.rubicon.game.IGame
import org.rubicon.game.impl.FieldCell
import kotlin.js.JsExport

@JsExport
class GameCellEvent(
    source: IGame,
    val fieldCell: FieldCell
) : Event<GameEventType, IGame>(GameEventType.CHANGE_CELL, source)