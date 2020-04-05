package it.aliut.ilserpente.user

import android.app.Application
import android.content.Context

interface UserRepository {
    fun getCurrentUser(): User
    fun updateCurrentUser(user: User)
}

class SharedPreferenceUserRepository(private val application: Application) : UserRepository {

    override fun getCurrentUser() = User(
        name = getPreferences().getString("username", User.DEFAULT_NAME) ?: User.DEFAULT_NAME,
        photoUrl = getPreferences().getString("photoUrl", null)
    )

    override fun updateCurrentUser(user: User) {
        with(getPreferences().edit()) {
            putString("username", user.name)
            putString("photoUrl", user.photoUrl)
            commit()
        }
    }

    private fun getPreferences() =
        application.getSharedPreferences(
            "ilserpente",
            Context.MODE_PRIVATE
        )
}
