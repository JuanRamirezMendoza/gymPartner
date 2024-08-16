package com.peakDevCol.gympartner.domain

import com.peakDevCol.gympartner.data.network.AuthService
import com.peakDevCol.gympartner.data.response.LoginResult
import javax.inject.Inject


/**
 * The UseCase are the actions that the user can do it.
 * In this case, for example, the user makes a login.
 */
class LoginUseCase @Inject constructor(private val authService: AuthService) {

    /**
     * invoke it's a type of function that you can use only call the instance of class
     * for example, loginUseCase(juan@prueba.com, 123456)
     **/
    suspend operator fun invoke(
        email: String?,
        password: String?,
        idTokenString: String ?
    ): LoginResult {
        return authService.login(email, password, idTokenString)
    }

}
