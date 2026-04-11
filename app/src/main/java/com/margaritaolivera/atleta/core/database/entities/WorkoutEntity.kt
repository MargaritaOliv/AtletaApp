package com.margaritaolivera.atleta.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_table")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val totalReps: Int,
    val distanceKm: Float,
    val isSynced: Boolean = false
)