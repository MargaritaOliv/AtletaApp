package com.margaritaolivera.atleta.features.training.data.local.dao

import androidx.room.*
import com.margaritaolivera.atleta.features.training.data.local.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: WorkoutSetEntity)

    @Query("SELECT * FROM workouts WHERE status = 'ACTIVE' LIMIT 1")
    suspend fun getActiveWorkout(): WorkoutEntity?

    @Query("SELECT * FROM workout_sets WHERE workoutId = :workoutId ORDER BY timestamp DESC")
    fun getSetsForWorkout(workoutId: Int): Flow<List<WorkoutSetEntity>>

    @Update
    suspend fun updateWorkout(workout: WorkoutEntity)

    @Query("DELETE FROM workouts")
    suspend fun clearAll()
}