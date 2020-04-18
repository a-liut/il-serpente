package it.aliut.ilserpente.user

import java.util.UUID

open class User(
    val id: String,
    val name: String,
    val photoUrl: String?
)

class Guest(name: String) : User(
    id = UUID.randomUUID().toString(),
    name = name,
    photoUrl = null
)
