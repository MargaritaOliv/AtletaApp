package com.margaritaolivera.atleta.features.training.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.margaritaolivera.atleta.core.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.margaritaolivera.atleta.features.social.domain.repositories.SocialRepository

data class AthleteProfileState(
    val name: String = "Atleta",
    val level: Int = 1,
    val xp: Int = 0,
    val maxXp: Int = 100,
    val lastSquatReps: Int = 0,
    val lastRunKm: Float = 0f,
    val hasPendingRequests: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val duelsWon: Int = 0,
    val duelsTotal: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val socialRepository: SocialRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow(AthleteProfileState())
    val profileState = _profileState.asStateFlow()

    init {
        loadProfile()
        checkPendingRequests()
    }

    private fun loadProfile() {
        val name = sessionManager.getName() ?: "Atleta"
        val level = sessionManager.getLevel()
        val xp = sessionManager.getXp()

        _profileState.value = _profileState.value.copy(
            name = name,
            level = if (level < 1) 1 else level,
            xp = xp,
            maxXp = 100,
            lastSquatReps = sessionManager.getLastSquatReps(),
            lastRunKm = sessionManager.getLastRunDistance(),
            duelsWon = sessionManager.getDuelsWon(),
            duelsTotal = sessionManager.getDuelsTotal()
        )
    }

    private fun checkPendingRequests() {
        viewModelScope.launch {
            socialRepository.getPendingRequests().fold(
                onSuccess = { list ->
                    _profileState.value = _profileState.value.copy(hasPendingRequests = list.isNotEmpty())
                },
                onFailure = {
                    _profileState.value = _profileState.value.copy(hasPendingRequests = false)
                }
            )
        }
    }

    fun refreshProfile() {
        loadProfile()
    }

    fun logout() {
        sessionManager.logout()
    }
}
