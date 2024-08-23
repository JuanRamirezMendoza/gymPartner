package com.peakDevCol.gympartner.data.response

data class BodyPartExerciseResponse(
    val bodyPart: String,
    val equipment: String,
    val gifUrl: String,
    val id: String,
    val name: String,
    val target: String,
    val secondaryMuscles: List<String>,
    val instructions: List<String>
)