package com.peakDevCol.gympartner.ui.basefirststepaccount

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peakDevCol.gympartner.core.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


open class BaseFirstStepAccountViewModel : ViewModel() {


    companion object {

        const val MIN_PASSWORD_LENGTH = 6

        private val _navigateToNextScreen = MutableLiveData<Event<String>>()
        val navigateToNextScreen: LiveData<Event<String>>
            get() = _navigateToNextScreen
    }

    private val _baseViewState = MutableStateFlow<BaseFirstStepAccountViewState?>(null)
    val baseViewState: StateFlow<BaseFirstStepAccountViewState?>
        get() = _baseViewState

    fun setBaseViewState(state: BaseFirstStepAccountViewState?) {
        _baseViewState.value = state
    }

    fun nextScreenSelected(screen: String) {
        _navigateToNextScreen.value = Event(screen)
    }

    //Revisar el funcionamiento con isEmpty()
    fun isValidOrEmptyEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()

}