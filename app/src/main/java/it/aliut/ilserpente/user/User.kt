package it.aliut.ilserpente.user

data class User(
    var name: String,
    var photoUrl: String? = null
) {
    companion object {
        const val DEFAULT_NAME = "Guest"
    }
}
