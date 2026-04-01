package com.margaritaolivera.atleta.features.social.data.repositories

import com.margaritaolivera.atleta.features.social.data.remote.api.SocialApi
import com.margaritaolivera.atleta.features.social.data.remote.model.FriendRequest
import com.margaritaolivera.atleta.features.social.domain.entities.Friend
import com.margaritaolivera.atleta.features.social.domain.entities.RankingAthlete
import com.margaritaolivera.atleta.features.social.domain.repositories.SocialRepository
import javax.inject.Inject

class SocialRepositoryImpl @Inject constructor(
    private val api: SocialApi
) : SocialRepository {

    override suspend fun sendFriendRequest(targetFirebaseUid: String): Result<Unit> = try {
        api.sendRequest(FriendRequest(targetFirebaseUid))
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getPendingRequests(): Result<List<Friend>> = try {
        val response = api.getPending()
        Result.success(response.map { Friend(it.id, it.nombre, it.nivel, it.fotoUrl, it.estado) })
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun acceptFriendRequest(friendshipId: Int): Result<Unit> = try {
        api.acceptRequest(friendshipId)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getFriendsList(): Result<List<Friend>> = try {
        val response = api.getFriends()
        Result.success(response.map { Friend(it.id, it.nombre, it.nivel, it.fotoUrl, it.estado) })
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getGlobalRanking(): Result<List<RankingAthlete>> = try {
        val response = api.getRanking()
        Result.success(response.mapIndexed { index, it ->
            RankingAthlete(it.id, it.nombre, it.nivel, it.experiencia, it.fotoUrl, index + 1)
        })
    } catch (e: Exception) {
        Result.failure(e)
    }
}