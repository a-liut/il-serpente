package it.aliut.ilserpente.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.aliut.ilserpente.user.SharedPreferenceUserRepository
import it.aliut.ilserpente.user.User

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = SharedPreferenceUserRepository(application)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    init {
        val storedUser = loadUser()

        _user.value = storedUser
    }

    fun setUsername(username: String) {
        _user.value!!.name = username

        saveUser()
    }

    private fun saveUser() {
        userRepository.updateCurrentUser(_user.value!!)
    }

    private fun loadUser(): User = userRepository.getCurrentUser()
}
