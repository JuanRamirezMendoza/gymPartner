package com.peakDevCol.gympartner.data.repository

import com.peakDevCol.gympartner.data.response.LoginResult

interface LoginRepository {
    suspend fun login(email: String?, password: String?, idTokenString: String?): LoginResult
}