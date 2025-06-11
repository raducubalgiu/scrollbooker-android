package com.example.scrollbooker.store.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.store.util.ThemePreferenceEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeDataStore: ThemeDataStore
): ViewModel() {
    val themePreferences: StateFlow<ThemePreferenceEnum> = themeDataStore.themePreferenceFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ThemePreferenceEnum.SYSTEM
        )

    fun updateThemePreference(themePreference: ThemePreferenceEnum) {
        viewModelScope.launch {
            themeDataStore.saveThemePreference(themePreference)
        }
    }
}