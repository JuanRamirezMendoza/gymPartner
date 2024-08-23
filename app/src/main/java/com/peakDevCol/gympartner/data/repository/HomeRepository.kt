package com.peakDevCol.gympartner.data.repository

import com.peakDevCol.gympartner.data.response.BodyPartExerciseResponse

interface HomeRepository {
    suspend fun callBodyPart(): List<String>

    suspend fun callBodyPartExercises(bodyPart: String): List<BodyPartExerciseResponse>
}