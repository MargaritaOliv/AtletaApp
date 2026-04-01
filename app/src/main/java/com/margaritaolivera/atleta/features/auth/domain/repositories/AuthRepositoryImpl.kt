package com.margaritaolivera.atleta.features.auth.data.repositories

import com.margaritaolivera.atleta.features.auth.data.remote.api.AuthApi
import com.margaritaolivera.atleta.features.auth.data.remote.model.*
import com.margaritaolivera.atleta.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun loginAndSync(): Result<AthleteResponse> = try {
        Result.success(api.loginSync())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updatePushToken(fcmToken: String): Result<Unit> = try {
        api.updateFcmToken(FcmTokenRequest(fcmToken))
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}