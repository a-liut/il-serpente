package it.aliut.ilserpente.service

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import it.aliut.ilserpente.model.MatchResult
import it.aliut.ilserpente.model.User
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

interface HistoryService {
    suspend fun getHistoryFor(user: User): List<MatchResult>
}

class FirebaseHistoryService : HistoryService {

    override suspend fun getHistoryFor(user: User): List<MatchResult> {
        return suspendCancellableCoroutine { continuation ->
            Firebase.firestore.collection("matches")
                .whereArrayContains("players", user.id)
                .get()
                .addOnSuccessListener { history ->
                    history.map { it.toObject(MatchResult::class.java) }
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }
}
