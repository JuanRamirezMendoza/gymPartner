package com.peakDevCol.gympartner.data.repository

import com.peakDevCol.gympartner.ui.signin.model.BaseUserSignIn

interface SignInRepository {
    suspend fun createAccount(userSignIn: BaseUserSignIn): Boolean

    suspend fun createUserTable(userSignIn: BaseUserSignIn): Boolean

}