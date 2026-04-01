package com.margaritaolivera.atleta.features.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margaritaolivera.atleta.features.auth.domain.repositories.AuthRepository
import com.margaritaolivera.atleta.features.auth.presentation.screens.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state = _state.asStateFlow()

    var emailLogin = MutableStateFlow("")
    var passwordLogin = MutableStateFlow("")

    var nameRegister = MutableStateFlow("")
    var emailRegister = MutableStateFlow("")
    var passwordRegister = MutableStateFlow("")

    fun loginAndSync() {
        val email = emailLogin.value.trim()
        val password = passwordLogin.value.trim()
        if (email.isEmpty() || password.isEmpty()) {
            _state.update { it.copy(error = "Llena todos los campos") }
            return
        }

        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            repository.loginAndSync(email, password).fold(
                onSuccess = { athleteData ->
                    _state.update { it.copy(isLoading = false, athlete = athleteData, isSuccess = true) }
                },
                onFailure = { exception ->
                    _state.update { it.copy(isLoading = false, error = exception.message) }
                }
            )
        }
    }

    fun registerAndSync() {
        val name = nameRegister.value.trim()
        val email = emailRegister.value.trim()
        val password = passwordRegister.value.trim()
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _state.update { it.copy(error = "Llena todos los campos") }
            return
        }

        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            repository.registerAndSync(name, email, password).fold(
                onSuccess = { athleteData ->
                    _state.update { it.copy(isLoading = false, athlete = athleteData, isSuccess = true) }
                },
                onFailure = { exception ->
                    _state.update { it.copy(isLoading = false, error = exception.message) }
                }
            )
        }
    }

    fun resetState() {
        _state.update { it.copy(isSuccess = false, error = null) }
    }
}