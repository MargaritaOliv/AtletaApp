package com.margaritaolivera.atleta.features.social.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margaritaolivera.atleta.core.session.SessionManager
import com.margaritaolivera.atleta.features.social.domain.entities.Friend
import com.margaritaolivera.atleta.features.social.domain.usecases.GetFriendsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DuelViewModel @Inject constructor(
    private val getFriendsListUseCase: GetFriendsListUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(DuelSelectionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        refreshDuelData()
    }

    fun refreshDuelData() {
        val currentXp = sessionManager.getXp()
        _uiState.update { it.copy(isLoading = true, userXp = currentXp) }
        
        viewModelScope.launch {
            getFriendsListUseCase().fold(
                onSuccess = { friends ->
                    _uiState.update { it.copy(isLoading = false, friends = friends, error = null) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }
}

data class DuelSelectionUiState(
    val isLoading: Boolean = false,
    val friends: List<Friend> = emptyList(),
    val userXp: Int = 0,
    val error: String? = null
)
