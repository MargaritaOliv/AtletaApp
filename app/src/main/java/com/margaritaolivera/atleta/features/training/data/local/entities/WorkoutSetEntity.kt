package com.margaritaolivera.atleta.features.training.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutSetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workoutId: Int,
    val reps: Int,
    val intensityScore: Double,
    val timestamp: Long
)