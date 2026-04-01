package com.margaritaolivera.atleta.features.social.data.remote.api

import com.margaritaolivera.atleta.features.social.data.remote.model.*
import retrofit2.http.*

interface SocialApi {
    @POST("api/v1/friends/request")
    suspend fun sendRequest(@Body request: FriendRequest)

    @GET("api/v1/friends/pending")
    suspend fun getPending(): List<FriendResponse>

    @PUT("api/v1/friends/accept/{id}")
    suspend fun acceptRequest(@Path("id") id: Int)

    @GET("api/v1/friends/list")
    suspend fun getFriends(): List<FriendResponse>

    @GET("api/v1/ranking/global")
    suspend fun getRanking(): List<RankingResponse>
}