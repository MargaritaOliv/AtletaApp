package com.margaritaolivera.atleta.features.auth.domain.usecases

import com.margaritaolivera.atleta.features.auth.domain.entities.Athlete
import com.margaritaolivera.atleta.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Athlete> {
        return repository.loginAndSync(email, password)
    }
}