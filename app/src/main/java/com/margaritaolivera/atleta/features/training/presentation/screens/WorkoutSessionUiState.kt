package com.margaritaolivera.atleta.features.training.presentation.screens

data class WorkoutSessionUiState(
    val isLoading: Boolean = false,
    val isTracking: Boolean = false,
    val workoutId: Int? = null,
    val currentReps: Int = 0,
    val totalReps: Int = 0,
    val error: String? = null,
    val isFinished: Boolean = false
)