package com.margaritaolivera.atleta.core.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithWorkouts(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val workouts: List<WorkoutEntity>
)