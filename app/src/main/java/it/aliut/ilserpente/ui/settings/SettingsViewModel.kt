package it.aliut.ilserpente.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import it.aliut.ilserpente.user.User
import it.aliut.ilserpente.user.UserRepository

class SettingsViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

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
