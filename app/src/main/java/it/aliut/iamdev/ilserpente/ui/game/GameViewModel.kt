package it.aliut.iamdev.ilserpente.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.aliut.iamdev.ilserpente.game.Board
import it.aliut.iamdev.ilserpente.game.GameMove
import it.aliut.iamdev.ilserpente.game.GameState
import it.aliut.iamdev.ilserpente.game.PlayerMove
import it.aliut.iamdev.ilserpente.game.player.ComputerPlayer
import it.aliut.iamdev.ilserpente.game.player.Player
import it.aliut.iamdev.ilserpente.utils.SingleLiveEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class GameViewModel : ViewModel() {

    /**
     * Indicates the current player.
     */
    private var currentPlayerIdx: Int = 0

    /**
     * True if the game is ended.
     */
    private var gameEnded: Boolean = true

    /**
     * Stores the players participating the game.
     */
    private val _players = MutableLiveData<List<Player>>()
    val players: LiveData<List<Player>>
        get() = _players

    /**
     * Stores the current player.
     */
    private val _currentPlayer = MutableLiveData<Player>()
    val currentPlayer: LiveData<Player>
        get() = _currentPlayer

    /**
     * Stores information about the state of the game.
     */
    private val _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState>
        get() = _gameState

    /**
     * Events
     */

    private val _onEndGameEvent = SingleLiveEvent<Boolean>()
    val onEndGameEvent: LiveData<Boolean>
        get() = _onEndGameEvent

    /**
     * Starts the game. It sets up players and a new game and starts the main game cycle.
     * Each player is asked in turn to produce a move. Then, the move is applied to the board.
     * If the game ends, it notifies the observers.
     */
    fun startGame(players: ArrayList<Player>) {
        setupPlayers(players)
        createGame()

        GlobalScope.launch {
            while (!gameEnded) {
                val currentPlayer = players[currentPlayerIdx]

                if (checkGameFinished(gameState.value!!)) {
                    Timber.d("Game finished!")
                    endGame()
                } else {
                    if (currentPlayer is ComputerPlayer) {
                        delay(500)
                        triggerMove(currentPlayer.getNextMove(_gameState.value!!))
                    }

                    _gameState.postValue(_gameState.value!!.copy(movesCount = _gameState.value!!.movesCount + 1))

                    nextPlayer()
                }
            }
        }
    }

    /**
     * Stops the game.
     */
    fun endGame() {
        gameEnded = true
        _onEndGameEvent.postValue(true)
    }

    /**
     * Returns true if the game is finished.
     */
    private fun checkGameFinished(gameState: GameState) = gameState.board.allowedMoves().isEmpty()

    /**
     * Creates the players.
     */
    private fun setupPlayers(newPlayers: ArrayList<Player>) {
        _players.value = newPlayers
    }

    /**
     * Creates a new game.
     */
    private fun createGame() {
        _gameState.value = GameState(0, Board(10, 10))

        gameEnded = false

        // First player
        currentPlayerIdx = -1
        nextPlayer()
    }

    /**
     * Trigger a game move by a player.
     */
    private fun triggerMove(move: GameMove): Boolean =
        _gameState.value!!.applyMove(PlayerMove(currentPlayer.value!!, move))


    /**
     * Switch current player.
     */
    private fun nextPlayer() {
        currentPlayerIdx = (currentPlayerIdx + 1) % _players.value!!.size
        _currentPlayer.postValue(_players.value!![currentPlayerIdx])
    }
}
