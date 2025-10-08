package com.example.scrollbooker.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.scrollbooker.navigation.bottomBar.MainTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TabsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private companion object { const val KEY_TAB = "current_tab" }

    private val _currentTab = MutableStateFlow(
        MainTab.fromRoute(savedStateHandle.get<String>(KEY_TAB))
    )
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    fun setTab(tab: MainTab) {
        if (_currentTab.value == tab) return
        _currentTab.value = tab
        savedStateHandle[KEY_TAB] = tab.route
    }
}