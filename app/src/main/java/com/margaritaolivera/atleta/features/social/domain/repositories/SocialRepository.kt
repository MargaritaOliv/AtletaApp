package com.margaritaolivera.atleta.features.social.domain.repositories

import com.margaritaolivera.atleta.features.social.domain.entities.Friend
import com.margaritaolivera.atleta.features.social.domain.entities.RankingAthlete

interface SocialRepository {
    suspend fun sendFriendRequest(email: String): Result<Unit>
    suspend fun getPendingRequests(): Result<List<Friend>>
    suspend fun acceptFriendRequest(friendshipId: Int): Result<Unit>
    suspend fun getFriendsList(): Result<List<Friend>>
    suspend fun getGlobalRanking(): Result<List<RankingAthlete>>
}