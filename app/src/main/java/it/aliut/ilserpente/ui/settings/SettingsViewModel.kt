package it.aliut.ilserpente.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import it.aliut.ilserpente.user.SharedPreferenceUserRepository
import it.aliut.ilserpente.user.User
import it.aliut.ilserpente.user.UserRepository

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository = SharedPreferenceUserRepository(application)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    init {
        _user.value = loadUser()
    }

    fun updateUser(account: GoogleSignInAccount?) {
        val updatedUser = User(
            name = account?.displayName ?: User.DEFAULT_NAME,
            photoUrl = account?.photoUrl?.toString()
        )

        userRepository.updateCurrentUser(updatedUser)
        _user.value = updatedUser
    }

    private fun loadUser(): User = userRepository.getCurrentUser()
}
