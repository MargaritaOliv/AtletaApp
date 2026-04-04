package com.margaritaolivera.atleta.features.social.data.remote.model

import com.google.gson.annotations.SerializedName

data class FriendRequest(
    val email: String
)

data class PendingRequestsResponse(
    val requests: List<PendingRequestDto>
)

data class PendingRequestDto(
    val id: Int,
    @SerializedName("firebase_uid") val firebaseUid: String?,
    val displayName: String,
    val level: Int
)

data class FriendsListResponse(
    val friends: List<FriendDto>
)

data class FriendDto(
    val id: Int,
    @SerializedName("firebase_uid") val firebaseUid: String?,
    val displayName: String,
    val level: Int,
    val experience: Int
)

data class AcceptFriendRequest(
    val requestId: Int,
    val userId: Int
)

data class RankingListResponse(
    val ranking: List<RankingDto>
)

data class RankingDto(
    val displayName: String,
    val level: Int,
    val experience: Int
)