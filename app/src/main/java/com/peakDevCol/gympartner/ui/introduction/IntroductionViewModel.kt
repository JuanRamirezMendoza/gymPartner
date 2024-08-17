package com.peakDevCol.gympartner.ui.introduction

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.peakDevCol.gympartner.core.Event
import com.peakDevCol.gympartner.data.response.LoginResult
import com.peakDevCol.gympartner.domain.LoginUseCase
import com.peakDevCol.gympartner.domain.ProviderTypeLogin
import com.peakDevCol.gympartner.domain.SaveAccountUseCase
import com.peakDevCol.gympartner.ui.signin.model.UserSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val authFireBase: FirebaseAuth,
    private val credentialManager: CredentialManager,
    private val loginUseCase: LoginUseCase,
    private val saveAccountUseCase: SaveAccountUseCase
) : ViewModel() {


    private val _viewState = MutableStateFlow<IntroductionViewState?>(null)
    val viewState: StateFlow<IntroductionViewState?>
        get() = _viewState

    private val _navigateToMenu = MutableLiveData<Event<Boolean>>()
    val navigateToMenu: LiveData<Event<Boolean>>
        get() = _navigateToMenu

    init {
        _navigateToMenu.value = Event(content = (authFireBase.currentUser != null))
    }

    private val _navigateToLogin = MutableLiveData<Event<Boolean>>()
    val navigateToLogin: LiveData<Event<Boolean>>
        get() = _navigateToLogin

    private val _navigateToSignIn = MutableLiveData<Event<Boolean>>()
    val navigateToSignIn: LiveData<Event<Boolean>>
        get() = _navigateToSignIn

    private val _googleSelected = MutableLiveData<Event<Boolean>>()
    val googleSelected: LiveData<Event<Boolean>>
        get() = _googleSelected


    fun onLoginSelected() {
        _navigateToLogin.value = Event(content = true)
    }


    fun onSingInSelected() {
        _navigateToSignIn.value = Event(content = true)
    }

    fun onGoogleSelected() {
        _googleSelected.value = Event(content = true)
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        _viewState.value = IntroductionViewState.Loading
        when (val credential = result.credential) {
            // GoogleIdToken credential
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        val idTokenString = googleIdTokenCredential.idToken
                        viewModelScope.launch {
                            when (loginUseCase(email = null, password = null, idTokenString)) {
                                LoginResult.Error -> {
                                    _viewState.value = IntroductionViewState.Error
                                    _viewState.value = null
                                }

                                is LoginResult.Success -> {
                                    val userData = authFireBase.currentUser!!
                                    val userSignIn = UserSignIn(
                                        userData.displayName ?: "",
                                        userData.email ?: "",
                                        ProviderTypeLogin.GOOGLE
                                    )
                                    saveAccountUseCase(userSignIn)
                                    _navigateToMenu.value = Event(true)
                                    _viewState.value = null
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("JFcatch", "Received an invalid google id token response", e)
                    }
                } else {
                    Log.e("JFelse", "CustomCredential")
                }
            }

            else -> {
                Log.e("JFelse", "Unexpected type of credential")
            }
        }
    }

    private fun handleFailure(e: GetCredentialException) {
        Log.e("GoogleSignIn", e.message.toString())
    }

    fun getCredentials(context: Context, request: GetCredentialRequest) {
        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                handleFailure(e)
            }

        }
    }

}