package com.margaritaolivera.atleta.core.navigation

import kotlinx.serialization.Serializable

sealed class Screens {
    @Serializable object Login
    @Serializable object Register
    @Serializable object Home
    @Serializable object Social

    @Serializable
    data class WorkoutSession(val type: String)

    @Serializable
    data class LiveDuel(val roomName: String)
}