package com.peakDevCol.gympartner.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.peakDevCol.gympartner.core.Event
import com.peakDevCol.gympartner.domain.CreateAccountUseCase
import com.peakDevCol.gympartner.ui.basefirststepaccount.BaseFirstStepAccountViewModel
import com.peakDevCol.gympartner.ui.basefirststepaccount.BaseFirstStepAccountViewState
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

    private val _navigateToHome = MutableLiveData<Event<Boolean>>()
    val navigateToHome: LiveData<Event<Boolean>>
        get() = _navigateToHome


    fun onFieldsChanged(userSignIn: FullUserSignIn) {
        _viewState.value = userSignIn.toSignInViewState()
    }

    private fun signInUser(
        userSignIn: FullUserSignIn,
        baseStateError: () -> Unit,
        baseStateLoading: (loading: BaseFirstStepAccountViewState?) -> Unit
    ) {
        viewModelScope.launch {
            baseStateLoading.invoke(BaseFirstStepAccountViewState.Loading)
            val accountCreated = createAccountUseCase(userSignIn)
            if (accountCreated) {
                _navigateToHome.value = Event(true)
                baseStateLoading.invoke(null)
            } else {
                baseStateError.invoke()
            }
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

    fun onSignInSelected(
        userSignIn: FullUserSignIn,
        baseStateError: () -> Unit,
        baseStateLoading: (loading: BaseFirstStepAccountViewState?) -> Unit
    ) {
        val viewState = userSignIn.toSignInViewState()
        if (viewState.userValidated() && userSignIn.isNotEmpty()) {
            signInUser(userSignIn, {
                baseStateError.invoke()
            }, {
                baseStateLoading.invoke(it)
            })
        } else {
            onFieldsChanged(userSignIn)
        }
    }


}



