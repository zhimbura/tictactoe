package org.rubicon.game.impl

import kotlin.js.JsExport

/**
 * Тип игрока
 * */
@JsExport
enum class PlayerType {
    /**
     * Игрок отсутствует
     * */
    NONE,
    /**
     * Игрок, который играет крестиками
     * */
    CROSS,
    /**
     * Игрок, который играет ноликами
     * */
    ZERO
}