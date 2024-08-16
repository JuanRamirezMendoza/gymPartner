package com.peakDevCol.gympartner.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.peakDevCol.gympartner.core.Event
import com.peakDevCol.gympartner.data.response.LoginResult
import com.peakDevCol.gympartner.domain.LoginUseCase
import com.peakDevCol.gympartner.ui.basefirststepaccount.BaseFirstStepAccountViewModel
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

    fun onLoginSelected(email: String, password: String) {
        if (isValidOrEmptyEmail(email) && isValidPassword(password)) {
            loginUser(email, password)
        } else {
            onFieldsChanged(email, password)
        }

    }

    /**
     *                      coroutine
     * I need to use a viewModelScope.launch because the functions that i call are suspend functions
     */
    private fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _viewState.value = LoginViewState(isLoading = true)
            when (loginUseCase(email, password,null)) {
                LoginResult.Error -> {
                    //Show error
                    _viewState.value = LoginViewState(isLoading = false)
                }

                is LoginResult.Success -> {
                    _navigateToHome.value = Event(true)
                }
            }
            _viewState.value = LoginViewState(isLoading = false)
        }

    }

    private fun isValidPassword(password: String): Boolean =
        password.length >= MIN_PASSWORD_LENGTH || password.isEmpty()


}