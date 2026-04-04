package com.margaritaolivera.atleta.features.auth.data.remote.model

import com.google.gson.annotations.SerializedName

data class FcmTokenRequest(
    @SerializedName("fcmToken") val fcmToken: String
)

data class LoginSyncResponse(
    val user: AthleteResponse
)

data class AthleteResponse(
    val id: Int,
    @SerializedName("displayName", alternate = ["nombre"]) val displayName: String,
    val email: String?,
    @SerializedName("level", alternate = ["nivel"]) val level: Int,
    @SerializedName("experience", alternate = ["experiencia"]) val experience: Int,
    @SerializedName("foto_url") val fotoUrl: String?
)