package com.margaritaolivera.atleta.features.social.domain.usecases

import com.margaritaolivera.atleta.features.social.domain.repositories.SocialRepository
import javax.inject.Inject

class AcceptFriendRequestUseCase @Inject constructor(
    private val repository: SocialRepository
) {
    suspend operator fun invoke(friendshipId: Int): Result<Unit> {
        return repository.acceptFriendRequest(friendshipId)
    }
}