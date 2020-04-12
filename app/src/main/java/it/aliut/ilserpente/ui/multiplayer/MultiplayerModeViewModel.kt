package it.aliut.ilserpente.ui.multiplayer

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.aliut.ilserpente.game.GameData
import it.aliut.ilserpente.game.GameMode
import it.aliut.ilserpente.game.player.HumanPlayer
import it.aliut.ilserpente.game.player.RemotePlayer
import it.aliut.ilserpente.service.FirebaseIlSerpenteService
import it.aliut.ilserpente.service.IlSerpenteService
import it.aliut.ilserpente.user.SharedPreferenceUserRepository
import it.aliut.ilserpente.user.UserRepository
import it.aliut.ilserpente.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MultiplayerModeViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository = SharedPreferenceUserRepository(application)

    private val ilSerpenteService: IlSerpenteService = FirebaseIlSerpenteService()

    private val _startGameEvent = SingleLiveEvent<GameData>()
    val startGameEvent: LiveData<GameData>
        get() = _startGameEvent

    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String>
        get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val scope = CoroutineScope(Dispatchers.IO)

    fun startRandom() {
        _loading.value = true

        scope.launch {
            val currentUser = getUser()

            try {
                val opponent = ilSerpenteService.getOpponent(currentUser)

                _startGameEvent.postValue(
                    GameData(
                        GameMode.SINGLE,
                        listOf(
                            HumanPlayer(currentUser, Color.RED),
                            RemotePlayer(opponent, Color.BLUE)
                        )
                    )
                )
            } catch (ex: Exception) {
                Timber.d("Error while fetching opponent: ${ex.message}")
                _error.postValue(ex.message)
            }
        }.invokeOnCompletion { _loading.postValue(false) }
    }

    private fun getUser() = userRepository.getCurrentUser()
}
