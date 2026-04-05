package com.margaritaolivera.atleta.features.training.presentation.screens

data class WorkoutSessionUiState(
    val isLoading: Boolean = false,
    val isTracking: Boolean = false,
    val isStarted: Boolean = false,
    val workoutId: Int? = null,
    val currentReps: Int = 0,
    val totalReps: Int = 0,
    val previousTotal: Float = 0f,
    val timerSeconds: Long = 0,
    val goalValue: Int = 0,
    val distanceKm: Float = 0f,
    val avgCadence: Float = 0f,
    val error: String? = null,
    val isFinished: Boolean = false,
    val isDuel: Boolean = false,
    val opponentName: String? = null,
    val opponentXp: Int = 0,
    val initialXpGap: Int = 0
)