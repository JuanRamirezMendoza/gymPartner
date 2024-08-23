package com.peakDevCol.gympartner.data.repository

import com.peakDevCol.gympartner.data.network.ExerciseService
import com.peakDevCol.gympartner.data.response.BodyPartExerciseResponse
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val exerciseService: ExerciseService) :
    HomeRepository {
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

}