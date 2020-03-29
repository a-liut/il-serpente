package it.aliut.iamdev.ilserpente.game.player

import it.aliut.iamdev.ilserpente.game.GameMove
import it.aliut.iamdev.ilserpente.game.GameState
import kotlinx.coroutines.delay

class ComputerPlayer(name: String, color: Int, private val playDelay: Long = 500) : Player(name, color) {

    override suspend fun getNextMove(gameState: GameState): GameMove {
        delay(playDelay)
        return gameState.board.allowedMoves().random()
    }
}
