package com.margaritaolivera.atleta.features.training.data.remote.api

import com.margaritaolivera.atleta.features.training.data.remote.model.*
import retrofit2.http.Body
import retrofit2.http.POST

interface TrainingApi {
    @POST("api/v1/workouts/start")
    suspend fun startWorkout(@Body request: StartWorkoutRequest): WorkoutResponse

    @POST("api/v1/workouts/add-set")
    suspend fun addSet(@Body request: AddSetRequest): AddSetResponse

    @POST("api/v1/workouts/finish")
    suspend fun finishWorkout(@Body request: FinishWorkoutRequest): FinishWorkoutResponse
}