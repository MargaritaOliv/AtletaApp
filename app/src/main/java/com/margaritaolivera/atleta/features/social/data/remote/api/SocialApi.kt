package com.margaritaolivera.atleta.features.social.data.remote.api

import com.margaritaolivera.atleta.features.social.data.remote.model.*
import retrofit2.http.*

interface SocialApi {
    @POST("api/v1/friends/request")
    suspend fun sendRequest(@Body request: FriendRequest)

    @GET("api/v1/friends/pending")
    suspend fun getPending(): PendingRequestsResponse

    @PUT("api/v1/friends/accept")
    suspend fun acceptRequest(@Body request: AcceptFriendRequest)

    @GET("api/v1/friends/list")
    suspend fun getFriends(): FriendsListResponse

    @GET("api/v1/ranking/global")
    suspend fun getRanking(): RankingListResponse
}