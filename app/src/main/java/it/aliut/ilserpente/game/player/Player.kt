package it.aliut.ilserpente.game.player

import it.aliut.ilserpente.game.GameMove
import it.aliut.ilserpente.game.GameState

abstract class Player(val name: String, val color: Int) {
    abstract suspend fun getNextMove(gameState: GameState): GameMove
}
