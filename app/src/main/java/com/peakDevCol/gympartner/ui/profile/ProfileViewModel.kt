package com.peakDevCol.gympartner.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel: ViewModel() {

    private val _data = MutableLiveData<String>()
    val data: LiveData<String> = _data

    fun updateData(newData: String){
        _data.value = newData
    }
}