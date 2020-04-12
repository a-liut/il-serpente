package it.aliut.ilserpente.game.player

import it.aliut.ilserpente.game.GameMove
import it.aliut.ilserpente.game.GameState
import it.aliut.ilserpente.user.User

class RemotePlayer(val user: User, color: Int) : Player(user.name, color) {
    override suspend fun getNextMove(gameState: GameState): GameMove {
        return gameState.board.allowedMoves().random() // TODO
    }
}
