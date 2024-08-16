package com.peakDevCol.gympartner.ui.signin.model

import com.peakDevCol.gympartner.domain.ProviderTypeLogin

sealed class BaseUserSignIn {
    abstract val fullName: String
    abstract val email: String
    abstract val type: ProviderTypeLogin
}

data class UserSignIn(
    override val fullName: String,
    override val email: String,
    override val type: ProviderTypeLogin
) : BaseUserSignIn()

data class FullUserSignIn(
    override val fullName: String,
    override val email: String,
    override val type: ProviderTypeLogin,
    val password: String = "",
    val passwordConfirmation: String = ""
) : BaseUserSignIn() {
    fun isNotEmpty() =
        fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && passwordConfirmation.isNotEmpty()
}
