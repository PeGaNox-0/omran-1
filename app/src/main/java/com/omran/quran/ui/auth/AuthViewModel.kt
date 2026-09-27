package com.omran.quran.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omran.quran.data.model.AppUser
import com.omran.quran.data.repository.AuthRepository
import com.omran.quran.util.toArabicMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val instituteName: String = "معهد المقرئ",
    val loading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    val user: StateFlow<AppUser?> = authRepository.currentUser.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null,
    )

    private val _state = MutableStateFlow(AuthUiState())
    val state = _state.asStateFlow()

    fun update(transform: (AuthUiState) -> AuthUiState) {
        _state.update { transform(it).copy(error = null) }
    }

    fun signIn() {
        val s = _state.value
        if (s.email.isBlank() || s.password.isBlank()) {
            _state.update { it.copy(error = "أدخل البريد وكلمة المرور") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            runCatching { authRepository.signIn(s.email, s.password) }
                .onFailure { e -> _state.update { it.copy(error = e.toArabicMessage()) } }
            _state.update { it.copy(loading = false) }
        }
    }

    fun register() {
        val s = _state.value
        if (s.name.isBlank() || s.email.isBlank() || s.password.length < 6) {
            _state.update { it.copy(error = "أكمل البيانات. كلمة المرور 6 أحرف على الأقل") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            runCatching {
                val instName = s.instituteName.ifBlank { "معهد المقرئ" }
                authRepository.registerAdmin(s.name, s.email, s.password, instName)
            }.onFailure { e -> _state.update { it.copy(error = e.toArabicMessage()) } }
            _state.update { it.copy(loading = false) }
        }
    }
}
