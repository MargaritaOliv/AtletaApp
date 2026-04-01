package com.margaritaolivera.atleta.features.social.domain.entities

data class Friend(
    val id: Int,
    val nombre: String,
    val nivel: Int,
    val fotoUrl: String?,
    val status: String
)

data class RankingAthlete(
    val id: Int,
    val nombre: String,
    val nivel: Int,
    val experiencia: Int,
    val fotoUrl: String?,
    val posicion: Int
)