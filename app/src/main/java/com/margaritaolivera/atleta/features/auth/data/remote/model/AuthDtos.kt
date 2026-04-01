package com.margaritaolivera.atleta.features.auth.data.remote.model

import com.google.gson.annotations.SerializedName

data class FcmTokenRequest(
    @SerializedName("fcm_token") val fcmToken: String
)

data class AthleteResponse(
    val id: Int,
    val nombre: String,
    val email: String,
    val nivel: Int,
    val experiencia: Int,
    @SerializedName("foto_url") val fotoUrl: String?
)