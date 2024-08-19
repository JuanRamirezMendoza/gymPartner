package com.peakDevCol.gympartner.domain

import com.peakDevCol.gympartner.data.repository.SignInRepository
import com.peakDevCol.gympartner.ui.signin.model.BaseUserSignIn
import javax.inject.Inject

class SaveAccountUseCase @Inject constructor(private val signInRepository: SignInRepository) {
    suspend operator fun invoke(userSignIn: BaseUserSignIn): Boolean {
        return signInRepository.createUserTable(userSignIn)
    }
}