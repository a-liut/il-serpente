package it.aliut.iamdev.ilserpente.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.aliut.iamdev.ilserpente.game.Board
import it.aliut.iamdev.ilserpente.game.GameEngine
import it.aliut.iamdev.ilserpente.game.GameMove
import it.aliut.iamdev.ilserpente.game.GameState
import it.aliut.iamdev.ilserpente.game.player.HumanPlayer
import it.aliut.iamdev.ilserpente.game.player.Player
import it.aliut.iamdev.ilserpente.utils.SingleLiveEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class GameViewModel : ViewModel(), GameEngine.Callback {

    companion object {
        const val DEFAULT_ROW_COUNT = 10
        const val DEFAULT_COLUMN_COUNT = 10
    }

    private lateinit var gameEngine: GameEngine

    private val _players = MutableLiveData<List<Player>>()
    val players: LiveData<List<Player>>
        get() = _players

    private val _currentPlayer = MutableLiveData<Player>()
    val currentPlayer: LiveData<Player>
        get() = _currentPlayer

    private val _winner = MutableLiveData<Player>()
    val winner: LiveData<Player>
        get() = _winner

    private val _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState>
        get() = _gameState

    /**
     * Events
     */

    private val _onEndGameEvent = SingleLiveEvent<Boolean>()
    val onEndGameEvent: LiveData<Boolean>
        get() = _onEndGameEvent

    private val _onExitGameEvent = SingleLiveEvent<Boolean>()
    val onExitGameEvent: LiveData<Boolean>
        get() = _onExitGameEvent

    /**
     * Starts the game. It sets up players and a new game and starts the main game cycle.
     * Each player is asked in turn to produce a move. Then, the move is applied to the board.
     * If the game ends, it notifies the observers.
     */
    fun startGame(players: ArrayList<Player>) {
        gameEngine = GameEngine(
            players,
            GameState(0, Board(DEFAULT_ROW_COUNT, DEFAULT_COLUMN_COUNT)),
            this
        )
        _players.value = gameEngine.players
        _currentPlayer.postValue(gameEngine.currentPlayer)

        gameEngine.start()
    }

    /**
     * Exits the game view.
     */
    fun exitGame() {
        _onExitGameEvent.postValue(gameEngine.gameEnded)
    }

    /**
     * Stops the game.
     */
    fun endGame() {
        gameEngine.endGame()
    }

    fun triggerPlayerMove(gameMove: GameMove) {
        GlobalScope.launch {
            (gameEngine.currentPlayer as? HumanPlayer)?.postMove(gameMove)
                ?: Timber.d("Not a human player")
        }
    }

    override fun onGameStateChanged(gameState: GameState) {
        _gameState.postValue(gameState)
    }

    override fun onCurrentPlayerChanged(player: Player) {
        _currentPlayer.postValue(player)
    }

    override fun onGameFinished() {
        _winner.postValue(gameEngine.currentPlayer)
        _onEndGameEvent.postValue(true)
    }
}
