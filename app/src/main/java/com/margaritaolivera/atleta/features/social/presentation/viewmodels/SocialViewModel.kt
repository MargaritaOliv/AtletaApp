package com.margaritaolivera.atleta.features.social.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margaritaolivera.atleta.features.social.domain.repositories.SocialRepository
import com.margaritaolivera.atleta.features.social.presentation.screens.SocialUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor(
    private val repository: SocialRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SocialUiState())
    val uiState = _uiState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow.asSharedFlow()

    init {
        loadAllSocialData()
    }

    fun loadAllSocialData() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val friendsDeferred = repository.getFriendsList()
            val pendingDeferred = repository.getPendingRequests()
            val rankingDeferred = repository.getGlobalRanking()

            _uiState.update { it.copy(
                isLoading = false,
                friends = friendsDeferred.getOrDefault(emptyList()),
                pendingRequests = pendingDeferred.getOrDefault(emptyList()),
                ranking = rankingDeferred.getOrDefault(emptyList())
            )}
        }
    }

    fun acceptFriend(friendshipId: Int) {
        viewModelScope.launch {
            repository.acceptFriendRequest(friendshipId).fold(
                onSuccess = { loadAllSocialData() },
                onFailure = { _errorFlow.emit("Error al aceptar solicitud") }
            )
        }
    }

    fun sendRequest(uid: String) {
        viewModelScope.launch {
            repository.sendFriendRequest(uid).fold(
                onSuccess = { _errorFlow.emit("Solicitud enviada correctamente") },
                onFailure = { _errorFlow.emit("No se pudo enviar la solicitud") }
            )
        }
    }
}