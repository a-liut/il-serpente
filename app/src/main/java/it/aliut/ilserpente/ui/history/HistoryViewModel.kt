package it.aliut.ilserpente.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.aliut.ilserpente.game.MatchResult

class HistoryViewModel : ViewModel() {

    private val _historyList = MutableLiveData<List<MatchResult>>()
    val historyList: LiveData<List<MatchResult>>
        get() = _historyList
}
