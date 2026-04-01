package com.margaritaolivera.atleta.features.training.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val remoteId: String? = null,
    val type: String,
    val startTime: Long,
    val endTime: Long? = null,
    val status: String = "ACTIVE"
)