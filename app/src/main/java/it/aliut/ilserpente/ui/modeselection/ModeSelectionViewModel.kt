package it.aliut.ilserpente.ui.modeselection

import android.app.Application
import android.content.Context
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import it.aliut.ilserpente.IlSerpenteApplication
import it.aliut.ilserpente.game.GameData
import it.aliut.ilserpente.game.GameMode
import it.aliut.ilserpente.game.player.ComputerPlayer
import it.aliut.ilserpente.game.player.HumanPlayer
import it.aliut.ilserpente.user.User
import it.aliut.ilserpente.utils.SingleLiveEvent

class ModeSelectionViewModel(application: Application) : AndroidViewModel(application) {

    private val _startGameEvent = SingleLiveEvent<GameData>()
    val startGameEvent: LiveData<GameData>
        get() = _startGameEvent

    fun startSingle() {
        _startGameEvent.value = GameData(
            GameMode.SINGLE,
            listOf(
                ComputerPlayer("(Computer) Player One", Color.RED),
                HumanPlayer(getUser().name, Color.BLUE)
            )
        )
    }

    fun start1v1() {
        _startGameEvent.value = GameData(
            GameMode.ONE_VS_ONE,
            listOf(
                HumanPlayer(getUser().name, Color.RED),
                HumanPlayer("Player Two", Color.BLUE)
            )
        )
    }

    private fun getUser() = User(
        name = getApplication<IlSerpenteApplication>().getSharedPreferences(
            "ilserpente",
            Context.MODE_PRIVATE
        )
            .getString("username", "guest") ?: "guest"
    )
}
