package com.peakDevCol.gympartner.data.network

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.peakDevCol.gympartner.data.response.LoginResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthService @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    /**
     * runCatching its the same that try catch but most efficient because i can get the
     * success or error result in the same place because
     */
    suspend fun login(email: String?, password: String?, idTokenString: String?): LoginResult =
        runCatching {
            if (idTokenString != null) {
                firebaseAuth.signInWithCredential(
                    GoogleAuthProvider.getCredential(
                        idTokenString,
                        null
                    )
                ).await()
            } else {
                firebaseAuth.signInWithEmailAndPassword(email!!, password!!).await()
            }
        }.toLoginResult()

    suspend fun createAccount(email: String, password: String): LoginResult = runCatching {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }.toLoginResult()


    /**
     * With this function i can change or map AuthResult to model LoginResult
     */
    private fun Result<AuthResult>.toLoginResult() = when (val result = getOrNull()) {
        null -> LoginResult.Error
        else -> {
            val userId = result.user
            //Make userId Null to see its behavior
            checkNotNull(userId)
            LoginResult.Success
        }
    }


}