package com.margaritaolivera.atleta.features.auth.data.repositories

import com.margaritaolivera.atleta.core.auth.FirebaseAuthManager
import com.margaritaolivera.atleta.core.session.SessionManager
import com.margaritaolivera.atleta.features.auth.data.remote.api.AuthApi
import com.margaritaolivera.atleta.features.auth.data.remote.model.*
import com.margaritaolivera.atleta.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val firebaseAuthManager: FirebaseAuthManager,
    private val sessionManager: SessionManager
) : AuthRepository {
    override suspend fun loginAndSync(email: String, password: String): Result<AthleteResponse> = try {
        // 1. Iniciar sesión en Firebase (Obtendrá token interno)
        val loginResult = firebaseAuthManager.login(email, password)
        if (loginResult.isFailure) throw Exception(loginResult.exceptionOrNull()?.message)
        
        // 2. Guardar Token en SessionManager para ser interceptado por Retrofit
        sessionManager.saveToken(loginResult.getOrThrow())
        
        // 3. Llamar API para Sync
        val apiResponse = api.loginSync()
        
        // 4. Guardar datos en la sesión (Si hay null cae a un valor por defecto derivado del email)
        val finalName = apiResponse.displayName ?: (email.substringBefore("@").replaceFirstChar { it.uppercase() })
        
        sessionManager.saveProfile(
            name = finalName,
            level = apiResponse.level,
            xp = apiResponse.experience
        )

        Result.success(apiResponse)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun registerAndSync(name: String, email: String, password: String): Result<AthleteResponse> = try {
        // 1. Crear usuario en Firebase
        val registerResult = firebaseAuthManager.register(email, password)
        if (registerResult.isFailure) throw Exception(registerResult.exceptionOrNull()?.message)

        // Opcional: Podríamos actualizar el 'displayName' en Firebase si la API no requiere pasarlo explícito en loginSync.
        // Dado que solo tienes endpoint loginSync(), asumimos que Firebase ya tendrá el record del correo, 
        // pero puedes ajustar tu backend para obtener el nombre si es necesario.
        
        // 2. Guardar Token en SessionManager
        sessionManager.saveToken(registerResult.getOrThrow())

        // 3. Sincronizar en la API (El mismo loginSync se encargará de crear el perfil SQL que el backend indica: Verifica token y sincroniza)
        val apiResponse = api.loginSync()
        
        // 4. Guardar datos en la sesión
        val finalName = apiResponse.displayName ?: (email.substringBefore("@").replaceFirstChar { it.uppercase() })
        
        sessionManager.saveProfile(
            name = finalName,
            level = apiResponse.level,
            xp = apiResponse.experience
        )

        Result.success(apiResponse)
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