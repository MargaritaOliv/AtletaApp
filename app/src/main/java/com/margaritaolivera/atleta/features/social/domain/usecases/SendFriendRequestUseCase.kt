package com.margaritaolivera.atleta.features.social.domain.usecases

import com.margaritaolivera.atleta.features.social.domain.repositories.SocialRepository
import javax.inject.Inject

class SendFriendRequestUseCase @Inject constructor(
    private val repository: SocialRepository
) {
    suspend operator fun invoke(targetFirebaseUid: String): Result<Unit> {
        return repository.sendFriendRequest(targetFirebaseUid)
    }
}