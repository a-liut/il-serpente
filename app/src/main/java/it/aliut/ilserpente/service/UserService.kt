package it.aliut.ilserpente.service

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import it.aliut.ilserpente.model.Guest
import it.aliut.ilserpente.model.User

abstract class UserService {
    abstract fun getCurrentUser(): User

    protected fun makeGuest() =
        Guest(GUEST_NAME)

    companion object {
        const val GUEST_NAME = "Guest"
    }
}

class GoogleUserService(
    private val application: Application
) : UserService() {

    override fun getCurrentUser(): User =
        GoogleSignIn.getLastSignedInAccount(application)
            ?.let { account ->
                User(
                    id = account.id ?: "noid",
                    name = account.displayName ?: "noname",
                    photoUrl = account.photoUrl.toString()
                )
            }
            ?: makeGuest()
}
