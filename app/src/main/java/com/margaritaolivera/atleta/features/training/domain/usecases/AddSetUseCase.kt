package com.margaritaolivera.atleta.features.training.domain.usecases

import com.margaritaolivera.atleta.features.training.data.remote.model.AddSetResponse
import com.margaritaolivera.atleta.features.training.domain.repositories.TrainingRepository
import javax.inject.Inject

class AddSetUseCase @Inject constructor(
    private val repository: TrainingRepository
) {
    suspend operator fun invoke(workoutId: Int, reps: Int, intensityScore: Float): Result<AddSetResponse> {
        return repository.addSet(workoutId, reps, intensityScore)
    }
}