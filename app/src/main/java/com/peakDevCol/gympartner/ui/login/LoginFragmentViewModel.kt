package com.peakDevCol.gympartner.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.peakDevCol.gympartner.core.Event
import com.peakDevCol.gympartner.data.response.LoginResult
import com.peakDevCol.gympartner.domain.LoginUseCase
import com.peakDevCol.gympartner.ui.basefirststepaccount.BaseFirstStepAccountViewModel
import com.peakDevCol.gympartner.ui.basefirststepaccount.BaseFirstStepAccountViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(val loginUseCase: LoginUseCase) :
    BaseFirstStepAccountViewModel() {

    private val _navigateToHome = MutableLiveData<Event<Boolean>>()
    val navigateToHome: LiveData<Event<Boolean>>
        get() = _navigateToHome

    private val _navigateToForgotPassword = MutableLiveData<Event<Boolean>>()
    val navigateToForgotPassword: LiveData<Event<Boolean>>
        get() = _navigateToForgotPassword

    private val _navigateToVerifyAccount = MutableLiveData<Event<Boolean>>()
    val navigateToVerifyAccount: LiveData<Event<Boolean>>
        get() = _navigateToVerifyAccount

    private val _viewState = MutableStateFlow(LoginViewState())
    val viewState: StateFlow<LoginViewState>
        get() = _viewState


    fun onFieldsChanged(email: String, password: String) {
        _viewState.value =
            LoginViewState(
                isValidEmail = isValidOrEmptyEmail(email),
                isValidPassword = isValidPassword(password)
            )

    }

    /**
     * Remember that in this place arrive de parameter for the lambda
     * In this case it a parameter the type Unit and BaseFirstStepAccountViewState
     * and if the lambda return a value for example -> (value of return)Int
     * in the last line into the lambda y need put de value of return for the example is a Int
     */
    fun onLoginSelected(
        email: String,
        password: String,
        baseStateError: () -> Unit,
        baseStateLoading: (loading: BaseFirstStepAccountViewState?) -> Unit
    ) {
        if (isValidOrEmptyEmail(email) && isValidPassword(password)) {
            loginUser(email, password, {
                baseStateError.invoke()
            }, {
                baseStateLoading.invoke(it)
            })
        } else {
            onFieldsChanged(email, password)
        }

    }

    /**
     *                      coroutine
     * I need to use a viewModelScope.launch because the functions that i call are suspend functions
     */
    private fun loginUser(
        email: String,
        password: String,
        baseStateError: () -> Unit,
        baseStateLoading: (loading: BaseFirstStepAccountViewState?) -> Unit
    ) {
        viewModelScope.launch {
            baseStateLoading.invoke(BaseFirstStepAccountViewState.Loading)
            when (loginUseCase(email, password, null)) {
                LoginResult.Error -> {
                    baseStateError.invoke()
                }

                is LoginResult.Success -> {
                    _navigateToHome.value = Event(true)
                }
            }
        }

    }

    private fun isValidPassword(password: String): Boolean =
        password.length >= MIN_PASSWORD_LENGTH || password.isEmpty()


}