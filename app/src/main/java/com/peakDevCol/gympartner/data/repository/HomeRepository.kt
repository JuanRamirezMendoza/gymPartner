package com.peakDevCol.gympartner.data.repository

import com.peakDevCol.gympartner.data.response.BodyPartExerciseResponse
import com.peakDevCol.gympartner.domain.model.BodyPartModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface HomeRepository {
    suspend fun callBodyPart(): List<String>

    suspend fun callBodyPartExercises(bodyPart: String):Response<List<BodyPartExerciseResponse>>

    suspend fun saveBodyPart(bodyPart: BodyPartModel)

    fun getLocalBodyPart(): Flow<BodyPartModel>
}