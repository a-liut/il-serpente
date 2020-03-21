package it.aliut.iamdev.ilserpente.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import it.aliut.iamdev.ilserpente.utils.SingleLiveEvent

class MainViewModel : ViewModel() {

    private val _startGameEvent = SingleLiveEvent<Boolean>()

    val startGameEvent: LiveData<Boolean>
        get() = _startGameEvent

    fun onStartGame() {
        _startGameEvent.value = true
    }
}
