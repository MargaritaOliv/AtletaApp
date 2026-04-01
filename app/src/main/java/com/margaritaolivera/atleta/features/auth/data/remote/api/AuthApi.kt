package com.margaritaolivera.atleta.features.auth.data.remote.api

import com.margaritaolivera.atleta.features.auth.data.remote.model.*
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApi {
    @POST("api/v1/auth/login")
    suspend fun loginSync(): AthleteResponse

    @PUT("api/v1/auth/fcm-token")
    suspend fun updateFcmToken(@Body request: FcmTokenRequest)
}