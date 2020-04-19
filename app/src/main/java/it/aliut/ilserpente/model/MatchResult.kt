package it.aliut.ilserpente.model

import it.aliut.ilserpente.game.player.Player
import java.util.Date

data class MatchResult(
    val players: List<Player>,
    val winner: Player,
    val moves: Int,
    val date: Date
)
