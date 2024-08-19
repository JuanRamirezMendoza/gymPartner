package com.peakDevCol.gympartner.data.repository

import android.util.Log
import com.peakDevCol.gympartner.data.network.AuthService
import com.peakDevCol.gympartner.data.network.UserSaveService
import com.peakDevCol.gympartner.data.response.LoginResult
import com.peakDevCol.gympartner.ui.signin.model.BaseUserSignIn
import com.peakDevCol.gympartner.ui.signin.model.FullUserSignIn
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val userSaveService: UserSaveService,
) : SignInRepository {
    override suspend fun createAccount(userSignIn: BaseUserSignIn): Boolean {
        return if (userSignIn is FullUserSignIn) {
            when (authService.createAccount(userSignIn.email, userSignIn.password)) {
                LoginResult.Error -> {
                    Log.e("ERROR", "usuario creado")
                    false
                }

                LoginResult.Success -> {
                    createUserTable(userSignIn = userSignIn)
                    true
                }
            }
        } else false
    }

    override suspend fun createUserTable(userSignIn: BaseUserSignIn): Boolean {
        val user = hashMapOf(
            "fullName" to userSignIn.fullName,
            "email" to userSignIn.email,
            "type" to userSignIn.type.name
        )
        if (userSignIn is FullUserSignIn) {
            user["password"] = userSignIn.password
            user["passwordConfirmation"] = userSignIn.passwordConfirmation
        }
        return userSaveService.createUserTable(user)
    }


}