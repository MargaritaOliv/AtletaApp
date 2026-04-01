package com.margaritaolivera.atleta.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.margaritaolivera.atleta.features.training.data.local.dao.WorkoutDao
import com.margaritaolivera.atleta.features.training.data.local.entities.*

@Database(
    entities = [WorkoutEntity::class, WorkoutSetEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AtletaDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}