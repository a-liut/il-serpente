package it.aliut.ilserpente.user

data class User(
    var name: String,
    var photoUrl: String?
) {
    companion object {
        const val DEFAULT_NAME = "Guest"
    }
}
