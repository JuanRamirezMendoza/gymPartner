package com.peakDevCol.gympartner.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peakDevCol.gympartner.data.local.entities.BodyPartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BodyPartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBodyPartList(bodyPartList: BodyPartEntity)

    @Query("SELECT * FROM body_part_table")
    fun getBodyPartList(): Flow<BodyPartEntity?>

}
