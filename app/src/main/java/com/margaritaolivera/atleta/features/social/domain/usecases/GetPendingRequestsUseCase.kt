package com.margaritaolivera.atleta.features.social.domain.usecases

import com.margaritaolivera.atleta.features.social.domain.entities.Friend
import com.margaritaolivera.atleta.features.social.domain.repositories.SocialRepository
import javax.inject.Inject

class GetPendingRequestsUseCase @Inject constructor(
    private val repository: SocialRepository
) {
    suspend operator fun invoke(): Result<List<Friend>> {
        return repository.getPendingRequests()
    }
}