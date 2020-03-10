package it.aliut.iamdev.ilserpente.game

import it.aliut.iamdev.ilserpente.game.player.Player

data class PlayerState(
    val player: Player,
    val isPlayerTurn: Boolean
)