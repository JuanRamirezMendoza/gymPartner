package com.peakDevCol.gympartner.data.network

import com.peakDevCol.gympartner.data.response.BodyPartExerciseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExerciseService {
    @GET("exercises/bodyPartList")
    suspend fun getBodyPartList(): Response<List<String>>

    /**
     * @Path is replace for the value of first parameter name
     * for example
     * getBodyPartExercises("back",10,0)
     * /exercises/bodyPart/back
     * and @Query is used to send information that backend need
     */
    @GET("/exercises/bodyPart/{bodyPart}")
    suspend fun getBodyPartExercises(
        @Path("bodyPart") name: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
    ): Response<List<BodyPartExerciseResponse>>

}
