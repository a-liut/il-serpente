package it.aliut.ilserpente.game

import it.aliut.ilserpente.game.player.HumanPlayer
import it.aliut.ilserpente.game.player.Player
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

    lateinit var currentPlayer: Player

    private var currentPlayerIdx: Int = -1

    init {
        nextPlayer()
    }

    fun start() {
        CoroutineScope(Dispatchers.Default).launch {
            runGame()
        }
    }

    private suspend fun runGame() {
        try {
            while (!gameEnded) {
                if (isGameFinished()) {
                    endGame()
                } else {
                    val move = PlayerMove(
                        currentPlayer,
                        currentPlayer.getNextMove(gameState)
                    )
                    if (gameState.isValidMove(move)) {
                        triggerMove(move)

                        nextPlayer()
                    } else {
                        Timber.d("Invalid move: $move")
                        callback.onInvalidMove(move)
                    }
                }
            }
        } catch (ex: ClosedReceiveChannelException) {
            Timber.d("Human Player interrupted")
        }

        Timber.d("Game thread exits!")
    }

    fun endGame() {
        if (!gameEnded) {
            gameEnded = true

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
        gameState = gameState.applyMove(nextMove)

        callback.onGameStateChanged(gameState)
    }

    private fun isGameFinished() = gameState.board.allowedMoves().isEmpty()

    /**
     * Switch current player.
     */
    private fun nextPlayer() {
        currentPlayerIdx = (currentPlayerIdx + 1) % players.size
        currentPlayer = players[currentPlayerIdx]

        callback.onCurrentPlayerChanged(currentPlayer)
    }
}
