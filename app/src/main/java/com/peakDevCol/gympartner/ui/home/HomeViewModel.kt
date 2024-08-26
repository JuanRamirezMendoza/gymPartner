package com.peakDevCol.gympartner.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.peakDevCol.gympartner.core.Event
import com.peakDevCol.gympartner.data.response.BodyPartExerciseResponse
import com.peakDevCol.gympartner.domain.BodyPartExerciseUseCase
import com.peakDevCol.gympartner.domain.ListBodyPartUseCase
import com.peakDevCol.gympartner.domain.LocalListBodyPartUseCase
import com.peakDevCol.gympartner.domain.SaveListBodyPartUseCase
import com.peakDevCol.gympartner.domain.model.BodyPartModel
import com.peakDevCol.gympartner.ui.basefirststepaccount.BaseFirstStepAccountViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val listBodyPartUseCase: ListBodyPartUseCase,
    private val saveListBodyPartUseCase: SaveListBodyPartUseCase,
    private val localListBodyPartUseCase: LocalListBodyPartUseCase,
    private val bodyPartExerciseUseCase: BodyPartExerciseUseCase,
    private val authFireBase: FirebaseAuth,
) : BaseFirstStepAccountViewModel() {

    private val _bodyPartState = MutableStateFlow<BodyPartModel?>(null)
    val bodyPartState: StateFlow<BodyPartModel?> = _bodyPartState

    private val _navigateToIntroduction = MutableLiveData<Event<Boolean>>()
    val navigateToIntroduction: LiveData<Event<Boolean>>
        get() = _navigateToIntroduction

    private val _bodyPart = MutableLiveData<List<String>>()
    val bodyPart: LiveData<List<String>>
        get() = _bodyPart

    private val _bodyPartExercises = MutableLiveData<List<BodyPartExerciseResponse>>()
    val bodyPartExercises: LiveData<List<BodyPartExerciseResponse>>
        get() = _bodyPartExercises

    suspend fun callBodyPart() {
        val bodyParts = listBodyPartUseCase()
        saveListBodyPartUseCase(BodyPartModel(bodyParts))
        _bodyPart.postValue(bodyParts)
    }

    suspend fun callBodyPartExerciseList(bodyPart: String) {
        _bodyPartExercises.postValue(bodyPartExerciseUseCase(bodyPart))
    }

    fun logOut() {
        if (authFireBase.currentUser != null) {
            authFireBase.signOut()
            _navigateToIntroduction.value = Event(true)
        }
    }

    fun getLocalBodyPart() {
        viewModelScope.launch {
            localListBodyPartUseCase().collect { bodyPart ->
                _bodyPartState.value = bodyPart
            }
        }
    }

}