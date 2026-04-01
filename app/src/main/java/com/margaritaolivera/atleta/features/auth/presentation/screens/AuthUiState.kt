package com.margaritaolivera.atleta.features.auth.presentation.screens

import com.margaritaolivera.atleta.features.auth.data.remote.model.AthleteResponse

data class AuthUiState(
    val isLoading: Boolean = false,
    val athlete: AthleteResponse? = null,
    val error: String? = null,
    val isSuccess: Boolean = false
)