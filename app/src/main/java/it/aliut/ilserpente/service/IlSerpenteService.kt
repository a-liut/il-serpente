package it.aliut.ilserpente.service

import it.aliut.ilserpente.user.User

interface IlSerpenteService {
    suspend fun getOpponent(user: User): User
}
