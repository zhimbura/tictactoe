package org.rubicon.game.impl.events

import org.rubicon.game.IGame
import org.rubicon.game.impl.FieldCell
import org.rubicon.game.impl.PlayerType
import kotlin.js.JsExport

@JsExport
class GameOverEvent(
    game: IGame,
    winLine: List<FieldCell>? = null
) : Event<GameEventType, IGame>(GameEventType.GAME_OVER, game) {
    val winLine = winLine?.toTypedArray()
    val winner: PlayerType = winLine?.first()?.getState() ?: PlayerType.NONE
}