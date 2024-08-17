package com.peakDevCol.gympartner.ui.signin

data class SignInViewState(
    val isValidFullName: Boolean = true,
    val isValidEmail: Boolean = true,
    val isValidPassword: Boolean = true
) {
    fun userValidated() = isValidEmail && isValidPassword && isValidFullName
}