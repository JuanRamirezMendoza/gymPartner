package com.peakDevCol.gympartner.data.repository

import com.peakDevCol.gympartner.data.network.ExerciseService
import com.peakDevCol.gympartner.data.response.BodyPartExerciseResponse
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val exerciseService: ExerciseService) :
    HomeRepository {
    override suspend fun callBodyPart(): List<String> {
        return exerciseService.getBodyPartList()
    }

    override suspend fun callBodyPartExercises(bodyPart: String): List<BodyPartExerciseResponse> {
        return exerciseService.getBodyPartExercises(bodyPart)
    }

}