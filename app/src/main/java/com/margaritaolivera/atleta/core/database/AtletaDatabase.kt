package com.margaritaolivera.atleta.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.margaritaolivera.atleta.core.database.entities.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AtletaDatabase : RoomDatabase() {
}