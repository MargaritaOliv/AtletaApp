package com.margaritaolivera.atleta.core.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthManager @Inject constructor() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: return Result.failure(Exception("Usuario no encontrado"))
            val token = user.getIdToken(true).await().token ?: return Result.failure(Exception("Sin token"))
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return Result.failure(Exception("No se pudo crear usuario"))
            val token = user.getIdToken(true).await().token ?: return Result.failure(Exception("Sin token"))
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFreshToken(): String? {
        return try {
            auth.currentUser?.getIdToken(true)?.await()?.token
        } catch (e: Exception) {
            null
        }
    }

    fun logout() {
        auth.signOut()
    }
}
