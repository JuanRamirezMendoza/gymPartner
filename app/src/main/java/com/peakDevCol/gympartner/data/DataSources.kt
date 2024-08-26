package com.peakDevCol.gympartner.data

import com.peakDevCol.gympartner.domain.model.BodyPartModel
import kotlinx.coroutines.flow.Flow


interface RemoteDataSource {
//TODO IMPLEMENT THE REMOTE METHODS calls from the API
}

interface LocalDataSource {
    suspend fun insertBodyPartList(bodyPart: BodyPartModel)
    fun getBodyPartList(): Flow<BodyPartModel>

}