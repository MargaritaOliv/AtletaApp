package com.margaritaolivera.atleta.features.social.domain.usecases

import com.margaritaolivera.atleta.features.social.domain.entities.RankingAthlete
import com.margaritaolivera.atleta.features.social.domain.repositories.SocialRepository
import javax.inject.Inject

class GetGlobalRankingUseCase @Inject constructor(
    private val repository: SocialRepository
) {
    suspend operator fun invoke(): Result<List<RankingAthlete>> {
        return repository.getGlobalRanking()
    }
}