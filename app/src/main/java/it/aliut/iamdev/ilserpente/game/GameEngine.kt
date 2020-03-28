package it.aliut.iamdev.ilserpente.game

import it.aliut.iamdev.ilserpente.game.player.HumanPlayer
import it.aliut.iamdev.ilserpente.game.player.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import timber.log.Timber

class GameEngine(
    var players: List<Player>,
    var gameState: GameState,
    private val callback: Callback
) {

    interface Callback {
        fun onGameStateChanged(gameState: GameState)
        fun onCurrentPlayerChanged(player: Player)
        fun onGameFinished()
        fun onInvalidMove(move: PlayerMove)
    }

    var gameEnded: Boolean = false

    var currentPlayer: Player? = null

    private var currentPlayerIdx: Int = -1

    fun start() {
        CoroutineScope(Dispatchers.Default).launch {
            var swapPlayer = true

            try {
                while (!gameEnded) {
                    if (isGameFinished(gameState)) {
                        endGame()
                    } else {
                        if (swapPlayer) nextPlayer()

                        val move = PlayerMove(
                            currentPlayer!!,
                            currentPlayer!!.getNextMove(gameState)
                        )
                        swapPlayer = if (gameState.isValidMove(move)) {
                            triggerMove(move)

                            true
                        } else {
                            Timber.d("Invalid move: $move")
                            callback.onInvalidMove(move)

                            false
                        }
                    }
                }
            } catch (ex: ClosedReceiveChannelException) {
                Timber.d("Human Player interrupted")
            }

            Timber.d("Game thread exits!")
        }
    }

    fun endGame() {
        if (!gameEnded) {
            gameEnded = true
            // supervisorJob.cancel()

            players
                .filterIsInstance<HumanPlayer>()
                .forEach { it.cancel() }

            nextPlayer()

            callback.onGameFinished()
        }
    }

    /**
     * Trigger a game move by a player.
     */
    private fun triggerMove(nextMove: PlayerMove) {
        gameState.applyMove(nextMove)

        gameState = gameState.copy(movesCount = gameState.movesCount + 1)
        callback.onGameStateChanged(gameState)
    }

    private fun isGameFinished(gameState: GameState) = gameState.board.allowedMoves().isEmpty()

    /**
     * Switch current player.
     */
    private fun nextPlayer() {
        currentPlayerIdx = (currentPlayerIdx + 1) % players.size
        currentPlayer = players[currentPlayerIdx]

        callback.onCurrentPlayerChanged(currentPlayer!!)
    }
}
