package com.peakDevCol.gympartner.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.peakDevCol.gympartner.core.Event
import com.peakDevCol.gympartner.core.ex.capitalizeFirstLetter
import com.peakDevCol.gympartner.data.response.BodyPartExerciseResponse
import com.peakDevCol.gympartner.domain.BodyPartExerciseUseCase
import com.peakDevCol.gympartner.domain.GetListBodyPartUseCase
import com.peakDevCol.gympartner.domain.ProviderTypeBodyPart
import com.peakDevCol.gympartner.domain.model.BodyPartModel
import com.peakDevCol.gympartner.ui.basefirststepaccount.BaseFirstStepAccountViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localListBodyPartUseCase: GetListBodyPartUseCase,
    private val bodyPartExerciseUseCase: BodyPartExerciseUseCase,
    private val authFireBase: FirebaseAuth,
) : BaseFirstStepAccountViewModel() {

    private val _viewState = MutableStateFlow<HomeViewState?>(null)
    val viewState: StateFlow<HomeViewState?>
        get() = _viewState

    private val _bodyPartState = MutableStateFlow<BodyPartModel?>(null)
    val bodyPartState: StateFlow<BodyPartModel?> = _bodyPartState

    private val _navigateToIntroduction = MutableLiveData<Event<Boolean>>()
    val navigateToIntroduction: LiveData<Event<Boolean>>
        get() = _navigateToIntroduction

    private val _bodyPartExercises = MutableLiveData<List<BodyPartExerciseResponse>>()
    val bodyPartExercises: LiveData<List<BodyPartExerciseResponse>>
        get() = _bodyPartExercises

    suspend fun callBodyPartExerciseList(bodyPart: ProviderTypeBodyPart) {
        _viewState.value = HomeViewState.Loading
        /*val response = bodyPartExerciseUseCase(bodyPart.name.lowercase().replace("_", " "))
        if (response.isSuccessful) {
            _bodyPartExercises.postValue(response.body() ?: emptyList())
        } else {
            _viewState.value = HomeViewState.Error
        }*/
        _viewState.value = null
    }

    fun logOut() {
        if (authFireBase.currentUser != null) {
            authFireBase.signOut()
            _navigateToIntroduction.value = Event(true)
        }
    }

    fun getLocalBodyPart() {
        _viewState.value = HomeViewState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            localListBodyPartUseCase().collect { bodyPartRoom ->
                _bodyPartState.value = bodyPartRoom
            }
            _viewState.value = null
        }
    }


    fun setHomeViewState(state: HomeViewState?) {
        _viewState.value = state
    }

    fun formatSecondaryMuscles(secondaryMuscles: List<String>): String {
        var muscles = ""
        secondaryMuscles.forEach {
            muscles += "-${it.capitalizeFirstLetter()}\n"
        }
        return muscles
    }

    fun formatInstructions(instructions: List<String>): String {
        var steps = ""
        var numberOfSteps = 1
        instructions.forEach {
            steps += "$numberOfSteps." + it.capitalizeFirstLetter() + "\n\n"
            numberOfSteps++
        }
        return steps
    }

}