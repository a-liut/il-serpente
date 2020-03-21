package it.aliut.iamdev.ilserpente.game

import timber.log.Timber

data class GameState(
    val movesCount: Int,
    val board: Board
) {
    fun applyMove(move: PlayerMove): Boolean {
        if (!isValidMove(move, board)) {
            Timber.d("Invalid move: $move")
            return false
        }

        return try {
            board.applyMove(move)

            true
        } catch (ex: MoveNotAllowedException) {
            false
        }
    }

    private fun isValidMove(move: PlayerMove, board: Board): Boolean =
        move.gameMove in board.allowedMoves()
}
