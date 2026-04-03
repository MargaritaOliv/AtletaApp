package com.margaritaolivera.atleta.features.training.domain.usecases

import com.margaritaolivera.atleta.features.training.data.remote.model.WorkoutResponse
import com.margaritaolivera.atleta.features.training.domain.repositories.TrainingRepository
import javax.inject.Inject

class StartWorkoutUseCase @Inject constructor(
    private val repository: TrainingRepository
) {
    suspend operator fun invoke(type: String): Result<WorkoutResponse> {
        return repository.startWorkout(type)
    }
}