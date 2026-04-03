package com.margaritaolivera.atleta.features.training.domain.repositories

import com.margaritaolivera.atleta.features.training.data.remote.model.*

interface TrainingRepository {
    suspend fun startWorkout(type: String): Result<WorkoutResponse>
    suspend fun addSet(workoutId: Int, reps: Int, intensityScore: Float): Result<AddSetResponse>
    suspend fun finishWorkout(workoutId: Int, totalDistance: Float, avgCadence: Float): Result<FinishWorkoutResponse>
}