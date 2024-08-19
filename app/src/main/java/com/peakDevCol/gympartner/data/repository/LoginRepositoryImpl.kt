package com.peakDevCol.gympartner.data.repository

import com.peakDevCol.gympartner.data.network.AuthService
import com.peakDevCol.gympartner.data.response.LoginResult
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val authService: AuthService) :
    LoginRepository {
    override suspend fun login(
        email: String?,
        password: String?,
        idTokenString: String?
    ): LoginResult {
        return authService.login(email, password, idTokenString)
    }

}