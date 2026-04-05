package com.margaritaolivera.atleta.features.auth.domain.entities

data class Athlete(
    val id: Int,
    val displayName: String,
    val email: String,
    val level: Int,
    val experience: Int,
    val fotoUrl: String?
)