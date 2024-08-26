package com.peakDevCol.gympartner.data.repository

import com.peakDevCol.gympartner.data.LocalDataSource
import com.peakDevCol.gympartner.data.network.ExerciseService
import com.peakDevCol.gympartner.data.response.BodyPartExerciseResponse
import com.peakDevCol.gympartner.domain.model.BodyPartModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val exerciseService: ExerciseService,
    private val local: LocalDataSource
) : HomeRepository {
    override suspend fun callBodyPart(): List<String> {
        val response = exerciseService.getBodyPartList()
        return when (response.code()) {
            200 -> response.body() ?: emptyList()
            else -> emptyList()
        }
    }

    override suspend fun callBodyPartExercises(bodyPart: String): List<BodyPartExerciseResponse> {
        val response = exerciseService.getBodyPartExercises(bodyPart)
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            throw Exception("Error fetching exercises for body part: ${response.code()}")
        }
    }

    override suspend fun saveBodyPart(bodyPart: BodyPartModel) {
        local.insertBodyPartList(bodyPart)
    }

    override fun getLocalBodyPart(): Flow<BodyPartModel> {
        return local.getBodyPartList()
    }

}