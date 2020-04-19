package it.aliut.ilserpente.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.aliut.ilserpente.model.User
import it.aliut.ilserpente.service.UserService

class SettingsViewModel(
    private val userService: UserService
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    init {
        updateUser()
    }

    fun updateUser() {
        val user = userService.getCurrentUser()
        _user.value = user
    }
}
