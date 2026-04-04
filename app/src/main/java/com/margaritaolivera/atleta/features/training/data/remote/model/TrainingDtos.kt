package com.margaritaolivera.atleta.features.training.data.remote.model

import com.google.gson.annotations.SerializedName

data class StartWorkoutRequest(
    val type: String
)

data class WorkoutResponse(
    @SerializedName("workoutId") val workoutId: Int,
    val status: String
)

data class AddSetRequest(
    @SerializedName("workoutId") val workoutId: Int,
    @SerializedName("repsCount") val reps: Int,
    @SerializedName("intensityScore") val intensityScore: Float
)

data class AddSetResponse(
    @SerializedName("currentExperience") val currentXp: Int,
    @SerializedName("currentLevel") val currentLevel: Int
)

data class FinishWorkoutRequest(
    @SerializedName("workoutId") val workoutId: Int,
    @SerializedName("totalReps") val totalReps: Int,
    @SerializedName("totalDistance") val totalDistance: Float,
    @SerializedName("avgCadence") val avgCadence: Float
)

data class FinishWorkoutResponse(
    val success: Boolean,
    val summary: String?
)