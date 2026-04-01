package com.margaritaolivera.atleta.core.network

import com.margaritaolivera.atleta.core.auth.FirebaseAuthManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authManager: FirebaseAuthManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val token = runBlocking { authManager.getFreshToken() }
        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}