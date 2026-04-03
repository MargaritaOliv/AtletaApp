package com.margaritaolivera.atleta.features.auth.domain.usecases

import com.margaritaolivera.atleta.features.auth.data.remote.model.AthleteResponse
import com.margaritaolivera.atleta.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<AthleteResponse> {
        return repository.registerAndSync(name, email, password)
    }
}
