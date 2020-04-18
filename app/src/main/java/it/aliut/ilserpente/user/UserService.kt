package it.aliut.ilserpente.user

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn

abstract class UserService {
    abstract fun getCurrentUser(): User

    protected fun makeGuest() = User(
        id = GUEST_ID,
        name = GUEST_NAME,
        photoUrl = null
    )

    companion object {
        const val GUEST_ID = "guestid"
        const val GUEST_NAME = "Guest"
    }
}

class FirebaseUserService(
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

fun User.isGuest(): Boolean = id == UserService.GUEST_ID
