package it.aliut.iamdev.ilserpente.game.player

import it.aliut.iamdev.ilserpente.game.GameMove
import it.aliut.iamdev.ilserpente.game.GameState

abstract class Player(val name: String, val color: Int) {
    abstract fun getNextMove(gameState: GameState): GameMove
}
