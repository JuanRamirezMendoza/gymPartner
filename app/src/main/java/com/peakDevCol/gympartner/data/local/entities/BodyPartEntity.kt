package com.peakDevCol.gympartner.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "body_part_table")
data class BodyPartEntity(@PrimaryKey @ColumnInfo(name = "body_parts") val bodyParts: List<String>)
