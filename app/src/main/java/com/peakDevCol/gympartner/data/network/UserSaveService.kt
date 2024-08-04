package com.peakDevCol.gympartner.data.network

import com.google.firebase.firestore.FirebaseFirestore
import com.peakDevCol.gympartner.ui.signin.model.UserSignIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserSaveService @Inject constructor(private val firebaseFireStore: FirebaseFirestore) {
    companion object {
        const val USER_COLLECTION = "users"
    }

    suspend fun createUserTable(userSignIn: UserSignIn) = runCatching {

        val user = hashMapOf(
            "fullName" to userSignIn.fullName,
            "email" to userSignIn.email,
            "password" to userSignIn.password,
            "passwordConfirmation" to userSignIn.passwordConfirmation,
        )

        firebaseFireStore.collection(USER_COLLECTION)
            .add(user).await()
    }.isSuccess

}