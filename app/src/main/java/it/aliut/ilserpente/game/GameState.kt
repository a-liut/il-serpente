package it.aliut.ilserpente.game

import timber.log.Timber

data class GameState(
    val movesCount: Int,
    val board: Board
) {
    fun applyMove(move: PlayerMove): GameState {
        if (!isValidMove(move)) {
            Timber.d("Invalid move: $move")
            return this
        }

        return try {
            board.applyMove(move)

            copy(movesCount = movesCount + 1)
        } catch (ex: MoveNotAllowedException) {
            this
        }
    }

    fun isValidMove(move: PlayerMove): Boolean =
        move.gameMove in board.allowedMoves()
}
