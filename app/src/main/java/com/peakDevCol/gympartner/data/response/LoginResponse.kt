package com.peakDevCol.gympartner.data.response

sealed class LoginResult {
    data object Error : LoginResult()
    data class Success(val verified: Boolean) : LoginResult()
}