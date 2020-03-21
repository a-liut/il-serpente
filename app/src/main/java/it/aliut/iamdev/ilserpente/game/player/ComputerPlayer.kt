package it.aliut.iamdev.ilserpente.game.player

import it.aliut.iamdev.ilserpente.game.GameMove
import it.aliut.iamdev.ilserpente.game.GameState

class ComputerPlayer(name: String, color: Int) : Player(name, color) {

    override fun getNextMove(gameState: GameState): GameMove =
        gameState.board.allowedMoves().random()
}
