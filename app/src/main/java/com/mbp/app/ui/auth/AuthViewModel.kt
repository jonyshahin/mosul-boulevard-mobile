package com.mbp.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbp.app.data.local.TokenManager
import com.mbp.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val userRole: String? = null,
    val initializing: Boolean = true,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val loggedIn = authRepository.isLoggedIn()
            val role = authRepository.getUserRole()
            _state.update {
                it.copy(isLoggedIn = loggedIn, userRole = role, initializing = false)
            }
        }
    }

    fun loginWithEmail(email: String, password: String) {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = authRepository.loginWithEmail(email, password)
            _state.update {
                if (result.isSuccess) {
                    it.copy(isLoading = false, isLoggedIn = true, userRole = "admin")
                } else {
                    it.copy(isLoading = false, error = result.exceptionOrNull()?.message)
                }
            }
        }
    }

    fun loginWithPhone(phone: String) {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = authRepository.loginWithPhone(phone)
            _state.update {
                if (result.isSuccess) {
                    it.copy(isLoading = false, isLoggedIn = true, userRole = "customer")
                } else {
                    it.copy(isLoading = false, error = result.exceptionOrNull()?.message)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _state.update { AuthUiState(initializing = false) }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
