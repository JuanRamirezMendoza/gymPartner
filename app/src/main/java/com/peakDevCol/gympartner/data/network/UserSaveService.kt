package com.peakDevCol.gympartner.data.network

import com.google.firebase.firestore.FirebaseFirestore
import com.peakDevCol.gympartner.domain.ProviderTypeLogin
import com.peakDevCol.gympartner.ui.signin.model.BaseUserSignIn
import com.peakDevCol.gympartner.ui.signin.model.FullUserSignIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserSaveService @Inject constructor(private val firebaseFireStore: FirebaseFirestore) {
    companion object {
        const val USER_COLLECTION = "USERS"
    }

    suspend fun createUserTable(user: HashMap<String, String>) = runCatching {
        val type = user["type"]
        val userCollectionRef = firebaseFireStore.collection(USER_COLLECTION)
            .document(type!!)
            .collection(type + USER_COLLECTION)

        if (type == ProviderTypeLogin.GOOGLE.name){
            val querySnapshot = userCollectionRef
                .whereEqualTo("email", user["email"])
                .get()
                .await()
            if (!querySnapshot.isEmpty) {
                throw Exception("El usuario ya existe en la colección.")
            }
        }
        userCollectionRef.add(user).await()

    }.isSuccess

}