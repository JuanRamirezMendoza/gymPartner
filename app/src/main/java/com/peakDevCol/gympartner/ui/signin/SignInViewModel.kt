package com.peakDevCol.gympartner.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.peakDevCol.gympartner.core.Event
import com.peakDevCol.gympartner.domain.CreateAccountUseCase
import com.peakDevCol.gympartner.ui.basefirststepaccount.BaseFirstStepAccountViewModel
import com.peakDevCol.gympartner.ui.signin.model.FullUserSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(val createAccountUseCase: CreateAccountUseCase) :
    BaseFirstStepAccountViewModel() {


    private val _viewState = MutableStateFlow(SignInViewState())
    val viewState: StateFlow<SignInViewState>
        get() = _viewState

    private val _navigateToOtherScreen = MutableLiveData<Event<Boolean>>()
    val navigateToOtherScreen: LiveData<Event<Boolean>>
        get() = _navigateToOtherScreen

    private var _showError = MutableLiveData(false)
    val showError: LiveData<Boolean>
        get() = _showError


    fun onFieldsChanged(userSignIn: FullUserSignIn) {
        _viewState.value = userSignIn.toSignInViewState()
    }

    private fun signInUser(userSignIn: FullUserSignIn) {
        viewModelScope.launch {
            _viewState.value = SignInViewState(isLoading = true)
            val accountCreated = createAccountUseCase(userSignIn)
            if (accountCreated) {
                _navigateToOtherScreen.value = Event(true)
            } else {
                _showError.value = true
            }
            _viewState.value = SignInViewState(isLoading = false)
        }
    }

    private fun FullUserSignIn.toSignInViewState(): SignInViewState {
        return SignInViewState(
            isValidFullName = isValidOrEmptyName(fullName),
            isValidEmail = isValidOrEmptyEmail(email),
            isValidPassword = isValidAndSamePassword(password, passwordConfirmation)
        )
    }

    private fun isValidAndSamePassword(password: String, passwordConfirmation: String): Boolean =
        password.isEmpty() || passwordConfirmation.isEmpty() || (password.length >= MIN_PASSWORD_LENGTH && password == passwordConfirmation)


    private fun isValidOrEmptyName(fullName: String): Boolean =
        fullName.isEmpty() || fullName.length >= MIN_PASSWORD_LENGTH

    fun onSignInSelected(userSignIn: FullUserSignIn) {
        val viewState = userSignIn.toSignInViewState()
        if (viewState.userValidated() && userSignIn.isNotEmpty()) {
            signInUser(userSignIn)
        } else {
            onFieldsChanged(userSignIn)
        }
    }

}



