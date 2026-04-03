package com.margaritaolivera.atleta.features.training.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margaritaolivera.atleta.core.hardware.domain.AccelerometerService
import com.margaritaolivera.atleta.core.hardware.domain.FlashService
import com.margaritaolivera.atleta.core.hardware.domain.VibrationService
import com.margaritaolivera.atleta.features.training.domain.usecases.AddSetUseCase
import com.margaritaolivera.atleta.features.training.domain.usecases.FinishWorkoutUseCase
import com.margaritaolivera.atleta.features.training.domain.usecases.StartWorkoutUseCase
import com.margaritaolivera.atleta.features.training.presentation.screens.WorkoutSessionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutSessionViewModel @Inject constructor(
    private val startWorkoutUseCase: StartWorkoutUseCase,
    private val addSetUseCase: AddSetUseCase,
    private val finishWorkoutUseCase: FinishWorkoutUseCase,
    private val accelerometerService: AccelerometerService,
    private val vibrationService: VibrationService,
    private val flashService: FlashService
) : ViewModel() {

    private val _state = MutableStateFlow(WorkoutSessionUiState())
    val state = _state.asStateFlow()

    private var isMovementLow = false

    fun startWorkout(type: String) {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            startWorkoutUseCase(type).fold(
                onSuccess = { response ->
                    _state.update { it.copy(isLoading = false, isTracking = true, workoutId = response.workoutId) }
                    startListeningSensors()
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    private fun startListeningSensors() {
        accelerometerService.startListening()
            .onEach { data ->
                if (data.y < 5.0f && !isMovementLow) {
                    isMovementLow = true
                } else if (data.y > 9.0f && isMovementLow) {
                    isMovementLow = false
                    registerRep()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun registerRep() {
        vibrationService.vibrateShort()
        _state.update { it.copy(currentReps = it.currentReps + 1) }
    }

    fun saveSet() {
        val currentWorkoutId = _state.value.workoutId ?: return
        val reps = _state.value.currentReps
        if (reps == 0) return

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            addSetUseCase(currentWorkoutId, reps, 1.5f).fold(
                onSuccess = {
                    flashService.blinkFlash(2, 100)
                    _state.update { it.copy(
                        isLoading = false,
                        totalReps = it.totalReps + reps,
                        currentReps = 0
                    )}
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    fun finishWorkout() {
        val currentWorkoutId = _state.value.workoutId ?: return
        accelerometerService.stopListening()

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            finishWorkoutUseCase(currentWorkoutId, 0f, 0f).fold(
                onSuccess = {
                    vibrationService.vibrateLong()
                    _state.update { it.copy(isLoading = false, isTracking = false, isFinished = true) }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        accelerometerService.stopListening()
    }
}