package com.margaritaolivera.atleta.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.margaritaolivera.atleta.core.database.entities.UserEntity
import com.margaritaolivera.atleta.core.database.entities.WorkoutEntity

@Database(
    entities = [UserEntity::class, WorkoutEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AtletaDatabase : RoomDatabase() {
    abstract fun atletaDao(): AtletaDao
}