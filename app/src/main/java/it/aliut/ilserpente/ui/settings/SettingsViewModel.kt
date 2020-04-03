package it.aliut.ilserpente.ui.settings

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.aliut.ilserpente.IlSerpenteApplication
import it.aliut.ilserpente.user.User

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    init {
        val storedUser = loadUser()

        _user.value = storedUser
    }

    override fun onCleared() {
        super.onCleared()

        saveUser()
    }

    fun setUsername(username: String) {
        _user.value!!.name = username
    }

    private fun saveUser() {
        val sharedPreference =
            getApplication<IlSerpenteApplication>().getSharedPreferences(
                "ilserpente",
                Context.MODE_PRIVATE
            )
        with(sharedPreference.edit()) {
            putString("username", _user.value!!.name)
            commit()
        }
    }

    private fun loadUser(): User = User(
        name = getApplication<IlSerpenteApplication>().getSharedPreferences(
            "ilserpente",
            Context.MODE_PRIVATE
        )
            .getString("username", "guest") ?: "guest"
    )
}
