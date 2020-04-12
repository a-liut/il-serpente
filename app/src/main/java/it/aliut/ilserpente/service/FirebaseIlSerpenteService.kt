package it.aliut.ilserpente.service

import it.aliut.ilserpente.user.User
import kotlinx.coroutines.delay

class FirebaseIlSerpenteService : IlSerpenteService {
    override suspend fun getOpponent(user: User): User {
        delay(3000)

        return User(
            name = "Random opponent",
            photoUrl = null
        )
    }
}
