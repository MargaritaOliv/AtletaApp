package com.margaritaolivera.atleta.features.social.data.remote.model

import com.google.gson.annotations.SerializedName

data class FriendRequest(
    @SerializedName("targetFirebaseUid") val targetUid: String
)

data class FriendResponse(
    val id: Int,
    val nombre: String,
    val nivel: Int,
    @SerializedName("foto_url") val fotoUrl: String?,
    val estado: String
)

data class RankingResponse(
    val id: Int,
    val nombre: String,
    val nivel: Int,
    val experiencia: Int,
    @SerializedName("foto_url") val fotoUrl: String?
)