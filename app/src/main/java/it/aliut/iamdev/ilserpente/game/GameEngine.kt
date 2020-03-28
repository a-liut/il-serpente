package it.aliut.iamdev.ilserpente.game

import it.aliut.iamdev.ilserpente.game.player.Player
import kotlinx.coroutines.GlobalScope
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

    private var currentPlayerIdx: Int = -1

    var currentPlayer: Player? = null

    init {
        nextPlayer()
    }

    fun start() {
        GlobalScope.launch {
            while (!gameEnded) {
                val currentPlayer = players[currentPlayerIdx]

                if (checkGameFinished(gameState)) {
                    Timber.d("Game finished!")
                    endGame()
                } else {
                    val move = PlayerMove(currentPlayer, currentPlayer.getNextMove(gameState))
                    if (gameState.isValidMove(move)) {
                        triggerMove(move)

                        nextPlayer()
                    } else {
                        Timber.d("Invalid move: $move")
                        callback.onInvalidMove(move)
                    }
                }
            }
        }
    }

    fun endGame() {
        if (!gameEnded) {
            gameEnded = true

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

    private fun checkGameFinished(gameState: GameState) = gameState.board.allowedMoves().isEmpty()

    /**
     * Switch current player.
     */
    private fun nextPlayer() {
        currentPlayerIdx = (currentPlayerIdx + 1) % players.size
        currentPlayer = players[currentPlayerIdx]

        callback.onCurrentPlayerChanged(currentPlayer!!)
    }
}
