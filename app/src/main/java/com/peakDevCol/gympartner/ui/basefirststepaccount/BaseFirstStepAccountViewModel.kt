package com.peakDevCol.gympartner.ui.basefirststepaccount

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peakDevCol.gympartner.core.Event


open class BaseFirstStepAccountViewModel : ViewModel() {


    companion object {

        const val MIN_PASSWORD_LENGTH = 6

        private val _navigateToNextScreen = MutableLiveData<Event<String>>()
        val navigateToNextScreen: LiveData<Event<String>>
            get() = _navigateToNextScreen
    }

    fun nextScreenSelected(screen: String) {
        _navigateToNextScreen.value = Event(screen)
    }

    //Revisar el funcionamiento con isEmpty()
    fun isValidOrEmptyEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()

}