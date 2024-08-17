package com.peakDevCol.gympartner.domain

import com.peakDevCol.gympartner.data.network.UserSaveService
import com.peakDevCol.gympartner.data.repository.SignInRepository
import com.peakDevCol.gympartner.ui.signin.model.BaseUserSignIn
import javax.inject.Inject

// UserSaveService is a class that contain the call to save data in fireStore and get a response

class CreateAccountUseCase @Inject constructor(private val signInRepository: SignInRepository) {
    suspend operator fun invoke(userSignIn: BaseUserSignIn): Boolean {
        return signInRepository.createAccount(userSignIn)
    }
}