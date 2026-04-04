package com.margaritaolivera.atleta.features.social.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margaritaolivera.atleta.features.social.domain.usecases.*
import com.margaritaolivera.atleta.features.social.presentation.screens.SocialUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor(
    private val getFriendsListUseCase: GetFriendsListUseCase,
    private val getPendingRequestsUseCase: GetPendingRequestsUseCase,
    private val getGlobalRankingUseCase: GetGlobalRankingUseCase,
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase
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
            val friendsResult = getFriendsListUseCase()
            val pendingResult = getPendingRequestsUseCase()
            val rankingResult = getGlobalRankingUseCase()

            _uiState.update { it.copy(
                isLoading = false,
                friends = friendsResult.getOrDefault(emptyList()),
                pendingRequests = pendingResult.getOrDefault(emptyList()),
                ranking = rankingResult.getOrDefault(emptyList())
            )}
        }
    }

    fun acceptFriend(friendshipId: Int) {
        viewModelScope.launch {
            acceptFriendRequestUseCase(friendshipId).fold(
                onSuccess = { loadAllSocialData() },
                onFailure = { _errorFlow.emit("Error al aceptar solicitud") }
            )
        }
    }

    fun sendRequest(email: String) {
        viewModelScope.launch {
            sendFriendRequestUseCase(email).fold(
                onSuccess = { _errorFlow.emit("Solicitud enviada correctamente") },
                onFailure = { _errorFlow.emit("No se pudo enviar la solicitud") }
            )
        }
    }
}