package com.margaritaolivera.atleta.features.auth.domain.repositories

import com.margaritaolivera.atleta.features.auth.domain.entities.Athlete

interface AuthRepository {
    suspend fun loginAndSync(email: String, password: String): Result<Athlete>
    suspend fun registerAndSync(name: String, email: String, password: String): Result<Athlete>
    suspend fun updatePushToken(fcmToken: String): Result<Unit>
}