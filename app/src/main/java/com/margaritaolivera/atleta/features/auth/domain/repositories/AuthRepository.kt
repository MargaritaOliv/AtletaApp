package com.margaritaolivera.atleta.features.auth.domain.repositories

import com.margaritaolivera.atleta.features.auth.data.remote.model.AthleteResponse

interface AuthRepository {
    suspend fun loginAndSync(): Result<AthleteResponse>
    suspend fun updatePushToken(fcmToken: String): Result<Unit>
}