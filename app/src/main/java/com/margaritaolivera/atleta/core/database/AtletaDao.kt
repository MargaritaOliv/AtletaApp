package com.margaritaolivera.atleta.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.margaritaolivera.atleta.core.database.entities.UserEntity
import com.margaritaolivera.atleta.core.database.entities.UserWithWorkouts
import com.margaritaolivera.atleta.core.database.entities.WorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AtletaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity)

    @Transaction
    @Query("SELECT * FROM user_table WHERE id = :userId")
    fun getUserWithWorkouts(userId: Int): Flow<UserWithWorkouts>

    @Query("SELECT * FROM workout_table WHERE isSynced = 0")
    suspend fun getUnsyncedWorkouts(): List<WorkoutEntity>

    @Query("UPDATE workout_table SET isSynced = 1 WHERE id = :workoutId")
    suspend fun markWorkoutAsSynced(workoutId: Int)
}