package it.aliut.ilserpente.game

import it.aliut.ilserpente.game.player.Player
import java.io.Serializable

data class GameData(
    val mode: GameMode,
    val players: List<Player>
) : Serializable
