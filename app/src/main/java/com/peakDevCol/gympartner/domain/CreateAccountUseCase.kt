package com.peakDevCol.gympartner.domain

import com.peakDevCol.gympartner.data.network.AuthService
import com.peakDevCol.gympartner.data.network.UserSaveService
import com.peakDevCol.gympartner.ui.signin.model.UserSignIn
import javax.inject.Inject

// UserSaveService it a class that contain the call to save data in fireStore and get a response

class CreateAccountUseCase @Inject constructor(
    private val authService: AuthService,
    private val userSaveService: UserSaveService
) {
    suspend operator fun invoke(userSignIn: UserSignIn): Boolean {
        val accountCreated =
            authService.createAccount(userSignIn.email, userSignIn.password) != null
        return if (accountCreated) {
            userSaveService.createUserTable(userSignIn)
        } else {
            false
        }
    }
}