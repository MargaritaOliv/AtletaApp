package com.margaritaolivera.atleta.features.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margaritaolivera.atleta.features.auth.domain.usecases.LoginUseCase
import com.margaritaolivera.atleta.features.auth.domain.usecases.RegisterUseCase
import com.margaritaolivera.atleta.features.auth.presentation.screens.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state = _state.asStateFlow()

    fun onEmailLoginChange(value: String) {
        _state.update { it.copy(emailLogin = value) }
    }

    fun onPasswordLoginChange(value: String) {
        _state.update { it.copy(passwordLogin = value) }
    }

    fun onNameRegisterChange(value: String) {
        _state.update { it.copy(nameRegister = value) }
    }

    fun onEmailRegisterChange(value: String) {
        _state.update { it.copy(emailRegister = value) }
    }

    fun onPasswordRegisterChange(value: String) {
        _state.update { it.copy(passwordRegister = value) }
    }

    fun loginAndSync() {
        val email = _state.value.emailLogin.trim()
        val password = _state.value.passwordLogin.trim()

        if (email.isEmpty() || password.isEmpty()) {
            _state.update { it.copy(error = "Llena todos los campos") }
            return
        }

        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            loginUseCase(email, password).fold(
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
        val name = _state.value.nameRegister.trim()
        val email = _state.value.emailRegister.trim()
        val password = _state.value.passwordRegister.trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _state.update { it.copy(error = "Llena todos los campos") }
            return
        }

        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            registerUseCase(name, email, password).fold(
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