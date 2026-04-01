package com.margaritaolivera.atleta.core.di

import android.content.Context
import androidx.room.Room
import com.margaritaolivera.atleta.core.database.AtletaDatabase
import com.margaritaolivera.atleta.features.training.data.local.dao.WorkoutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AtletaDatabase {
        return Room.databaseBuilder(
            context,
            AtletaDatabase::class.java,
            "atleta_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideWorkoutDao(db: AtletaDatabase): WorkoutDao {
        return db.workoutDao()
    }
}