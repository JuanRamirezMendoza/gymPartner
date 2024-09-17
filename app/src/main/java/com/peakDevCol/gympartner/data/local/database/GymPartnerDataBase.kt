package com.peakDevCol.gympartner.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peakDevCol.gympartner.data.local.dao.BodyPartDao
import com.peakDevCol.gympartner.data.local.entities.BodyPartEntity

@Database(entities = [BodyPartEntity::class], version = 1)
@TypeConverters(TypeResponseConverter::class)
abstract class GymPartnerDataBase : RoomDatabase() {
    abstract fun bodyPartDao(): BodyPartDao

}