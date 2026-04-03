package com.margaritaolivera.atleta.features.training.domain.usecases

import com.margaritaolivera.atleta.features.training.data.remote.model.FinishWorkoutResponse
import com.margaritaolivera.atleta.features.training.domain.repositories.TrainingRepository
import javax.inject.Inject

class FinishWorkoutUseCase @Inject constructor(
    private val repository: TrainingRepository
) {
    suspend operator fun invoke(workoutId: Int, totalDistance: Float, avgCadence: Float): Result<FinishWorkoutResponse> {
        return repository.finishWorkout(workoutId, totalDistance, avgCadence)
    }
}