package com.peakDevCol.gympartner.data.response

sealed class LoginResult {
    data object Error : LoginResult()
    data object Success: LoginResult()
}