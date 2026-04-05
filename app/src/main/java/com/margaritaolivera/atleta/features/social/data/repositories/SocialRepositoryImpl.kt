package com.margaritaolivera.atleta.features.social.data.repositories

import com.margaritaolivera.atleta.core.session.SessionManager
import com.margaritaolivera.atleta.features.social.data.remote.api.SocialApi
import com.margaritaolivera.atleta.features.social.data.remote.model.AcceptFriendRequest
import com.margaritaolivera.atleta.features.social.data.remote.model.FriendRequest
import com.margaritaolivera.atleta.features.social.domain.entities.Friend
import com.margaritaolivera.atleta.features.social.domain.entities.RankingAthlete
import com.margaritaolivera.atleta.features.social.domain.repositories.SocialRepository
import javax.inject.Inject

class SocialRepositoryImpl @Inject constructor(
    private val api: SocialApi,
    private val sessionManager: SessionManager
) : SocialRepository {

    override suspend fun sendFriendRequest(email: String): Result<Unit> = try {
        api.sendRequest(FriendRequest(email))
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getPendingRequests(): Result<List<Friend>> = try {
        val response = api.getPending()
        Result.success(response.requests.map { Friend(it.id, it.displayName, it.level, null, "PENDING", 0) })
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun acceptFriendRequest(friendshipId: Int): Result<Unit> = try {
        val userId = sessionManager.getUserId()
        api.acceptRequest(AcceptFriendRequest(requestId = friendshipId, userId = userId))
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getFriendsList(): Result<List<Friend>> = try {
        val response = api.getFriends()
        Result.success(response.friends.map { Friend(it.id, it.displayName, it.level, null, "ACCEPTED", it.experience) })
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getGlobalRanking(): Result<List<RankingAthlete>> = try {
        val response = api.getRanking()
        Result.success(response.ranking.mapIndexed { index, it ->
            RankingAthlete(0, it.displayName, it.level, it.experience, null, index + 1)
        })
    } catch (e: Exception) {
        Result.failure(e)
    }
}