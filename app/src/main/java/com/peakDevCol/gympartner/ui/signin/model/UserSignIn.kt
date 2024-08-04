package com.peakDevCol.gympartner.ui.signin.model

data class UserSignIn(
    val fullName: String,
    val email: String,
    val password: String,
    val passwordConfirmation: String
) {

    fun isNotEmpty() =
        fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && passwordConfirmation.isNotEmpty()



}
