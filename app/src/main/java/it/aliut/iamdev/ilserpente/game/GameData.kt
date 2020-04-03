package it.aliut.iamdev.ilserpente.game

import it.aliut.iamdev.ilserpente.game.player.Player
import java.io.Serializable

data class GameData(
    val mode: GameMode,
    val players: List<Player>
) : Serializable
