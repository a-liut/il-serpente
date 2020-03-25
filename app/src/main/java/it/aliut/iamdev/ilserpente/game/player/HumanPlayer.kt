package it.aliut.iamdev.ilserpente.game.player

import it.aliut.iamdev.ilserpente.game.GameMove
import it.aliut.iamdev.ilserpente.game.GameState
import kotlinx.coroutines.channels.Channel

class HumanPlayer(name: String, color: Int) : Player(name, color) {

    private val moveInputChannel = Channel<GameMove>()

    override suspend fun getNextMove(gameState: GameState): GameMove =
        moveInputChannel.receive()

    suspend fun postMove(gameMove: GameMove) {
        moveInputChannel.send(gameMove)
    }
}
