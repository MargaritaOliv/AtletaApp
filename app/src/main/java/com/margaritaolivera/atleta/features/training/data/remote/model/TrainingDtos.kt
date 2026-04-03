package com.margaritaolivera.atleta.features.training.data.remote.model

data class StartWorkoutRequest(
    val type: String
)

data class WorkoutResponse(
    val workoutId: Int,
    val status: String
)

data class AddSetRequest(
    val workoutId: Int,
    val reps: Int,
    val intensityScore: Float
)

data class AddSetResponse(
    val currentXp: Int,
    val currentLevel: Int
)

data class FinishWorkoutRequest(
    val workoutId: Int,
    val totalDistance: Float,
    val avgCadence: Float
)

data class FinishWorkoutResponse(
    val success: Boolean,
    val summary: String?
)