package com.peakDevCol.gympartner.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.peakDevCol.gympartner.core.Event
import com.peakDevCol.gympartner.data.response.BodyPartExerciseResponse
import com.peakDevCol.gympartner.domain.BodyPartExerciseUseCase
import com.peakDevCol.gympartner.domain.ListBodyPartUseCase
import com.peakDevCol.gympartner.ui.basefirststepaccount.BaseFirstStepAccountViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val listBodyPartUseCase: ListBodyPartUseCase,
    private val mechanicFilterUseCase: BodyPartExerciseUseCase,
    private val authFireBase: FirebaseAuth,
) : BaseFirstStepAccountViewModel() {


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
        _bodyPart.postValue(listBodyPartUseCase())
    }

    suspend fun callBodyPartExerciseList(bodyPart: String) {
        _bodyPartExercises.postValue(mechanicFilterUseCase(bodyPart))
    }

    fun logOut() {
        if (authFireBase.currentUser != null) {
            authFireBase.signOut()
            _navigateToIntroduction.value = Event(true)
        }
    }

}