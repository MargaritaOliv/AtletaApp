package com.margaritaolivera.atleta.features.auth.domain.repositories

import com.margaritaolivera.atleta.features.auth.data.remote.model.AthleteResponse

interface AuthRepository {
    suspend fun loginAndSync(email: String, password: String): Result<AthleteResponse>
    suspend fun registerAndSync(name: String, email: String, password: String): Result<AthleteResponse>
    suspend fun updatePushToken(fcmToken: String): Result<Unit>
}