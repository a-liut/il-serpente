package it.aliut.ilserpente.user

import android.app.Application
import android.content.Context

interface UserRepository {
    fun getCurrentUser(): User
    fun updateCurrentUser(user: User)
}

class SharedPreferenceUserRepository(private val application: Application) : UserRepository {

    override fun getCurrentUser() = User(
        name = getPreferences().getString("username", "guest") ?: "guest"
    )

    override fun updateCurrentUser(user: User) {
        with(getPreferences().edit()) {
            putString("username", user.name)
            commit()
        }
    }

    private fun getPreferences() =
        application.getSharedPreferences(
            "ilserpente",
            Context.MODE_PRIVATE
        )
}
