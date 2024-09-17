package com.peakDevCol.gympartner.data.local

import com.peakDevCol.gympartner.data.LocalDataSource
import com.peakDevCol.gympartner.data.local.database.GymPartnerDataBase
import com.peakDevCol.gympartner.domain.model.BodyPartModel
import kotlinx.coroutines.flow.Flow

class RoomDataSource(database: GymPartnerDataBase) : LocalDataSource {

    private val bodyPartDao by lazy { database.bodyPartDao() }

    override suspend fun insertBodyPartList(bodyPart: BodyPartModel) {
        bodyPartDao.insertBodyPartList(bodyPart.toEntity())
    }

    override fun getBodyPartList(): Flow<BodyPartModel> {
        return bodyPartDao.getBodyPartList().transform { bodyPartEntity ->
            BodyPartModel(bodyParts = bodyPartEntity?.bodyParts ?: listOf())
        }
    }
}