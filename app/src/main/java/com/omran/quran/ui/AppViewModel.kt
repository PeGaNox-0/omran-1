package com.omran.quran.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omran.quran.data.model.Session
import com.omran.quran.data.repository.AuthRepository
import com.omran.quran.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    authRepository: AuthRepository,
    settingsRepository: SettingsRepository,
) : ViewModel() {
    val session = authRepository.session.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        Session.Loading,
    )
    val darkTheme = settingsRepository.darkTheme.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null,
    )
}
