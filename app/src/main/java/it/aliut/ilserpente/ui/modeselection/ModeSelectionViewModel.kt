package it.aliut.ilserpente.ui.modeselection

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import it.aliut.ilserpente.game.GameData
import it.aliut.ilserpente.game.GameMode
import it.aliut.ilserpente.game.player.ComputerPlayer
import it.aliut.ilserpente.game.player.HumanPlayer
import it.aliut.ilserpente.model.User
import it.aliut.ilserpente.service.UserService
import it.aliut.ilserpente.utils.SingleLiveEvent

class ModeSelectionViewModel(
    private val userService: UserService
) : ViewModel() {

    private val _startGameEvent = SingleLiveEvent<GameData>()
    val startGameEvent: LiveData<GameData>
        get() = _startGameEvent

    fun startSingle() {
        _startGameEvent.value = GameData(
            GameMode.SINGLE,
            listOf(
                ComputerPlayer("(Computer) Player One", Color.RED),
                HumanPlayer(getUser(), Color.BLUE)
            )
        )
    }

    fun start1v1() {
        _startGameEvent.value = GameData(
            GameMode.ONE_VS_ONE,
            listOf(
                HumanPlayer(getUser(), Color.RED),
                HumanPlayer(
                    User(
                        id = "noid",
                        name = "Player Two",
                        photoUrl = null
                    ), Color.BLUE
                )
            )
        )
    }

    private fun getUser() = userService.getCurrentUser()
}
