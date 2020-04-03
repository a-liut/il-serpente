package it.aliut.ilserpente.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import it.aliut.ilserpente.utils.SingleLiveEvent

class MainViewModel : ViewModel() {

    private val _startEvent = SingleLiveEvent<Boolean>()
    val startEvent: LiveData<Boolean>
        get() = _startEvent

    fun onStartGame() {
        _startEvent.value = true
    }
}
