package it.aliut.iamdev.ilserpente.ui.modeselection

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import it.aliut.iamdev.ilserpente.game.GameData
import it.aliut.iamdev.ilserpente.game.GameMode
import it.aliut.iamdev.ilserpente.game.player.ComputerPlayer
import it.aliut.iamdev.ilserpente.game.player.HumanPlayer
import it.aliut.iamdev.ilserpente.utils.SingleLiveEvent

class ModeSelectionViewModel : ViewModel() {

    private val _startGameEvent = SingleLiveEvent<GameData>()

    val startGameEvent: LiveData<GameData>
        get() = _startGameEvent

    fun startSingle() {
        _startGameEvent.value = GameData(
            GameMode.SINGLE,
            listOf(
                ComputerPlayer("(Computer) Player One", Color.RED),
                HumanPlayer("(Human) Player Two", Color.BLUE)
            )
        )
    }

    fun start1v1() {
        _startGameEvent.value = GameData(
            GameMode.ONE_VS_ONE,
            listOf(
                HumanPlayer("(Human) Player One", Color.RED),
                HumanPlayer("(Human) Player Two", Color.BLUE)
            )
        )
    }
}
