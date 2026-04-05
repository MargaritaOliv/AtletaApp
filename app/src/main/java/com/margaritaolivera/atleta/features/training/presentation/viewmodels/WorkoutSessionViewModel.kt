package com.margaritaolivera.atleta.features.training.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margaritaolivera.atleta.core.hardware.domain.AccelerometerService
import com.margaritaolivera.atleta.core.hardware.domain.FlashService
import com.margaritaolivera.atleta.core.hardware.domain.VibrationService
import com.margaritaolivera.atleta.core.session.SessionManager
import com.margaritaolivera.atleta.features.training.domain.usecases.AddSetUseCase
import com.margaritaolivera.atleta.features.training.domain.usecases.FinishWorkoutUseCase
import com.margaritaolivera.atleta.features.training.domain.usecases.StartWorkoutUseCase
import com.margaritaolivera.atleta.features.training.presentation.screens.WorkoutSessionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sqrt

@HiltViewModel
class WorkoutSessionViewModel @Inject constructor(
    private val startWorkoutUseCase: StartWorkoutUseCase,
    private val addSetUseCase: AddSetUseCase,
    private val finishWorkoutUseCase: FinishWorkoutUseCase,
    private val accelerometerService: AccelerometerService,
    private val vibrationService: VibrationService,
    private val flashService: FlashService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(WorkoutSessionUiState())
    val state = _state.asStateFlow()

    private var isMovementLow = false
    private var timerJob: Job? = null
    private var currentWorkoutType: String = ""

    private val strideLength = mapOf("RUN" to 1.2f, "JOG" to 0.8f)

    fun prepareWorkout(type: String, opponentName: String? = null, opponentXp: Int = 0) {
        currentWorkoutType = type
        val currentXp = sessionManager.getXp()
        val initialGap = if (opponentName != null) opponentXp - currentXp else 0
        
        val previousTotal = when (type) {
            "SQUAT" -> sessionManager.getLastSquatReps().toFloat()
            else -> sessionManager.getLastRunDistance()
        }
        _state.update {
            it.copy(
                goalValue = sessionManager.getGoal(type),
                previousTotal = previousTotal,
                isDuel = opponentName != null,
                opponentName = opponentName,
                opponentXp = opponentXp,
                initialXpGap = initialGap,
                error = null
            )
        }
    }

    fun startTracking() {
        if (_state.value.isTracking) return
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            startWorkoutUseCase(currentWorkoutType).fold(
                onSuccess = { response ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isTracking = true,
                            isStarted = true,
                            workoutId = response.workoutId
                        )
                    }
                    vibrationService.vibrateShort()
                    flashService.blinkFlash(1, 200)
                    startTimer()
                    startListeningSensors()
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _state.update { it.copy(timerSeconds = it.timerSeconds + 1) }
                updateCadence()
            }
        }
    }

    private fun updateCadence() {
        val mins = _state.value.timerSeconds / 60.0f
        if (mins > 0) {
            val totalStepsOrReps = if (currentWorkoutType == "SQUAT") {
                _state.value.totalReps + _state.value.currentReps
            } else {
                (_state.value.distanceKm * 1000 / (strideLength[currentWorkoutType] ?: 1.0f)).toInt()
            }
            _state.update { it.copy(avgCadence = totalStepsOrReps / mins) }
        }
    }

    private fun startListeningSensors() {
        accelerometerService.startListening()
            .onEach { data ->
                if (currentWorkoutType == "SQUAT") {
                    handleSquat(data.y)
                } else {
                    handleStep(data.x, data.y, data.z)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun handleSquat(y: Float) {
        if (y < 5.0f && !isMovementLow) {
            isMovementLow = true
        } else if (y > 9.0f && isMovementLow) {
            isMovementLow = false
            registerRep()
        }
    }

    private fun handleStep(x: Float, y: Float, z: Float) {
        val magnitude = sqrt(x * x + y * y + z * z)
        if (magnitude > 13.0f && !isMovementLow) {
            isMovementLow = true
            registerStep()
        } else if (magnitude < 11.0f) {
            isMovementLow = false
        }
    }

    private fun registerStep() {
        val stepMeters = strideLength[currentWorkoutType] ?: 0.7f
        _state.update {
            val nextDist = it.distanceKm + (stepMeters / 1000f)
            // Estimación: 1 XP cada 10 metros
            val xpGained = if (it.isDuel) (stepMeters / 10f).toInt() else 0
            it.copy(
                distanceKm = nextDist,
                initialXpGap = (it.initialXpGap - xpGained).coerceAtLeast(0)
            )
        }
    }

    private fun registerRep() {
        vibrationService.vibrateShort()
        _state.update {
            val nextReps = it.currentReps + 1
            // Estimación: 2 XP por sentadilla
            val xpGained = if (it.isDuel) 2 else 0
            it.copy(
                currentReps = nextReps,
                initialXpGap = (it.initialXpGap - xpGained).coerceAtLeast(0)
            )
        }
    }

    fun saveSet() {
        val currentWorkoutId = _state.value.workoutId ?: return
        val reps = _state.value.currentReps
        if (reps == 0) return

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            addSetUseCase(currentWorkoutId, reps, 1.5f).fold(
                onSuccess = { response ->
                    flashService.blinkFlash(2, 100)
                    sessionManager.saveProfile(
                        name = sessionManager.getName() ?: "",
                        level = response.currentLevel,
                        xp = response.currentXp
                    )
                    _state.update {
                        it.copy(
                            isLoading = false,
                            totalReps = it.totalReps + reps,
                            currentReps = 0
                        )
                    }
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
        timerJob?.cancel()

        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val currentReps = _state.value.currentReps
            val totalReps = _state.value.totalReps

            if (currentWorkoutType == "SQUAT") {
                if (currentReps > 0) {
                    addSetUseCase(currentWorkoutId, currentReps, 1.5f).fold(
                        onSuccess = { response ->
                            sessionManager.saveProfile(
                                name = sessionManager.getName() ?: "",
                                level = response.currentLevel,
                                xp = response.currentXp
                            )
                        },
                        onFailure = { }
                    )
                }
            } else {
                val distanceMeters = (_state.value.distanceKm * 1000).toInt()
                if (distanceMeters > 0) {
                    val intensity = if (currentWorkoutType == "RUN") 1.0f else 0.7f
                    addSetUseCase(currentWorkoutId, distanceMeters, intensity).fold(
                        onSuccess = { response ->
                            sessionManager.saveProfile(
                                name = sessionManager.getName() ?: "",
                                level = response.currentLevel,
                                xp = response.currentXp
                            )
                        },
                        onFailure = { }
                    )
                }
            }

            val finalReps = totalReps + currentReps
            val finalDistance = _state.value.distanceKm
            val finalCadence = _state.value.avgCadence

            finishWorkoutUseCase(currentWorkoutId, finalReps, finalDistance, finalCadence).fold(
                onSuccess = {
                    vibrationService.vibrateLong()
                    flashService.blinkFlash(3, 100)

                    if (currentWorkoutType == "SQUAT") {
                        sessionManager.saveLastWorkoutStat("SQUAT", finalReps.toFloat())
                    } else {
                        sessionManager.saveLastWorkoutStat(currentWorkoutType, finalDistance)
                    }

                    checkObjective(finalReps, finalDistance)

                    if (_state.value.isDuel) {
                        val won = _state.value.initialXpGap <= 0
                        sessionManager.incrementDuelsPlayed(won)
                    }

                    _state.update { it.copy(isLoading = false, isTracking = false, isFinished = true) }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    private fun checkObjective(finalReps: Int, finalDistance: Float) {
        val goalValue = _state.value.goalValue
        val reached = if (currentWorkoutType == "SQUAT") {
            finalReps >= goalValue
        } else {
            (finalDistance * 1000) >= goalValue
        }

        if (reached) {
            val increment = if (currentWorkoutType == "SQUAT") 10 else 500
            sessionManager.saveGoal(currentWorkoutType, goalValue + increment)
        }
    }

    override fun onCleared() {
        super.onCleared()
        accelerometerService.stopListening()
        timerJob?.cancel()
    }
}