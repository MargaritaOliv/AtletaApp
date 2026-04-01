package com.margaritaolivera.atleta.features.training.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.margaritaolivera.atleta.core.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class AthleteProfileState(
    val name: String = "Atleta",
    val level: Int = 1,
    val xp: Int = 0,
    val maxXp: Int = 1000,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _profileState = MutableStateFlow(AthleteProfileState())
    val profileState = _profileState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        val name = sessionManager.getName() ?: "Atleta"
        val level = sessionManager.getLevel()
        val xp = sessionManager.getXp()

        _profileState.value = AthleteProfileState(
            name = name,
            level = if (level < 1) 1 else level,
            xp = xp,
            maxXp = 1000 * (if (level < 1) 1 else level),
            isLoading = false
        )
    }

    fun logout() {
        sessionManager.logout()
    }
}
