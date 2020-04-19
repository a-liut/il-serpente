package it.aliut.ilserpente.game.player

import it.aliut.ilserpente.game.GameMove
import it.aliut.ilserpente.game.GameState
import it.aliut.ilserpente.model.User
import kotlinx.coroutines.channels.Channel

class HumanPlayer(val user: User, color: Int) : Player(user.name, color) {

    private val moveInputChannel = Channel<GameMove>()

    override suspend fun getNextMove(gameState: GameState): GameMove =
        moveInputChannel.receive()

    suspend fun postMove(gameMove: GameMove) {
        moveInputChannel.send(gameMove)
    }

    fun cancel() {
        moveInputChannel.close()
    }
}
