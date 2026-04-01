package com.margaritaolivera.atleta.features.social.presentation.screens

import com.margaritaolivera.atleta.features.social.domain.entities.Friend
import com.margaritaolivera.atleta.features.social.domain.entities.RankingAthlete

data class SocialUiState(
    val isLoading: Boolean = false,
    val friends: List<Friend> = emptyList(),
    val pendingRequests: List<Friend> = emptyList(),
    val ranking: List<RankingAthlete> = emptyList(),
    val isSuccess: Boolean = false
)