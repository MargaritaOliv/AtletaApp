package com.margaritaolivera.atleta.features.training.data.repositories

import com.margaritaolivera.atleta.core.database.AtletaDao
import com.margaritaolivera.atleta.core.database.entities.WorkoutEntity
import com.margaritaolivera.atleta.core.session.SessionManager
import com.margaritaolivera.atleta.features.training.data.remote.api.TrainingApi
import com.margaritaolivera.atleta.features.training.data.remote.model.*
import com.margaritaolivera.atleta.features.training.domain.repositories.TrainingRepository
import javax.inject.Inject

class TrainingRepositoryImpl @Inject constructor(
    private val api: TrainingApi,
    private val sessionManager: SessionManager,
    private val atletaDao: AtletaDao
) : TrainingRepository {

    override suspend fun startWorkout(type: String): Result<WorkoutResponse> = try {
        val response = api.startWorkout(StartWorkoutRequest(type))
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addSet(workoutId: Int, reps: Int, intensityScore: Float): Result<AddSetResponse> = try {
        val response = api.addSet(AddSetRequest(workoutId, reps, intensityScore))
        sessionManager.saveProfile(
            name = sessionManager.getName() ?: "",
            level = response.currentLevel,
            xp = response.currentXp
        )
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun finishWorkout(workoutId: Int, totalReps: Int, totalDistance: Float, avgCadence: Float): Result<FinishWorkoutResponse> = try {
        val response = api.finishWorkout(FinishWorkoutRequest(workoutId, totalReps, totalDistance, avgCadence))

        val userId = sessionManager.getUserId()
        if (userId != -1) {
            atletaDao.insertWorkout(
                WorkoutEntity(
                    userId = userId,
                    totalReps = totalReps,
                    distanceKm = totalDistance
                )
            )
        }

        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
}