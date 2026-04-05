package com.margaritaolivera.atleta.features.auth.presentation.screens

import com.margaritaolivera.atleta.features.auth.domain.entities.Athlete

data class AuthUiState(
    val isLoading: Boolean = false,
    val athlete: Athlete? = null,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val emailLogin: String = "",
    val passwordLogin: String = "",
    val nameRegister: String = "",
    val emailRegister: String = "",
    val passwordRegister: String = ""
)