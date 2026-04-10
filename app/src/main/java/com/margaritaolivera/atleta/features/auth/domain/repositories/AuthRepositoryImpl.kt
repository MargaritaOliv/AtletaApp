package com.margaritaolivera.atleta.features.auth.data.repositories

import com.google.firebase.messaging.FirebaseMessaging
import com.margaritaolivera.atleta.core.auth.FirebaseAuthManager
import com.margaritaolivera.atleta.core.session.SessionManager
import com.margaritaolivera.atleta.features.auth.data.remote.api.AuthApi
import com.margaritaolivera.atleta.features.auth.data.remote.model.FcmTokenRequest
import com.margaritaolivera.atleta.features.auth.domain.entities.Athlete
import com.margaritaolivera.atleta.features.auth.domain.repositories.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val firebaseAuthManager: FirebaseAuthManager,
    private val sessionManager: SessionManager
) : AuthRepository {
    override suspend fun loginAndSync(email: String, password: String): Result<Athlete> = try {
        // Limpieza previa para asegurar un estado limpio antes del nuevo login
        sessionManager.logout()
        
        val loginResult = firebaseAuthManager.login(email, password)
        if (loginResult.isFailure) throw Exception(loginResult.exceptionOrNull()?.message)

        sessionManager.saveToken(loginResult.getOrThrow())

        val apiResponse = api.loginSync().user

        val previousUserId = sessionManager.getUserId()
        if (previousUserId != -1 && previousUserId != apiResponse.id) {
            sessionManager.clearAllWorkoutData()
        }
        sessionManager.saveUserId(apiResponse.id)

        val savedName = sessionManager.getName()
        val finalName = if (previousUserId == apiResponse.id && savedName != null) {
            savedName
        } else {
            apiResponse.displayName?.takeIf { it.isNotBlank() && it != "Atleta" }
                ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }
        }

        sessionManager.saveProfile(
            name = finalName,
            level = apiResponse.level,
            xp = apiResponse.experience
        )

        // Sincronización CRÍTICA del Token FCM
        try {
            val fcmToken = FirebaseMessaging.getInstance().token.await()
            updatePushToken(fcmToken)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val athlete = Athlete(
            id = apiResponse.id,
            displayName = finalName,
            email = apiResponse.email ?: email,
            level = apiResponse.level,
            experience = apiResponse.experience,
            fotoUrl = apiResponse.fotoUrl
        )

        Result.success(athlete)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun registerAndSync(name: String, email: String, password: String): Result<Athlete> = try {
        sessionManager.logout()

        val registerResult = firebaseAuthManager.register(email, password, name)
        if (registerResult.isFailure) throw Exception(registerResult.exceptionOrNull()?.message)

        sessionManager.saveToken(registerResult.getOrThrow())

        val apiResponse = api.loginSync().user

        val previousUserId = sessionManager.getUserId()
        if (previousUserId != -1 && previousUserId != apiResponse.id) {
            sessionManager.clearAllWorkoutData()
        }
        sessionManager.saveUserId(apiResponse.id)

        val finalName = apiResponse.displayName?.takeIf { it.isNotBlank() && it != "Atleta" } ?: name

        sessionManager.saveProfile(
            name = finalName,
            level = apiResponse.level,
            xp = apiResponse.experience
        )

        try {
            val fcmToken = FirebaseMessaging.getInstance().token.await()
            updatePushToken(fcmToken)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val athlete = Athlete(
            id = apiResponse.id,
            displayName = finalName,
            email = apiResponse.email ?: email,
            level = apiResponse.level,
            experience = apiResponse.experience,
            fotoUrl = apiResponse.fotoUrl
        )

        Result.success(athlete)
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