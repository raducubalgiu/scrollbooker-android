package com.example.scrollbooker.screens.profile.myProfile.settings.reportProblem.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.screens.profile.myProfile.settings.reportProblem.domain.useCase.SendProblemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportAProblemViewModel @Inject constructor(
    private val sendProblemUseCase: SendProblemUseCase
): ViewModel() {

    private val _reportProblemState = MutableStateFlow<FeatureState<Unit>?>(null)
    val reportAProblemState: MutableStateFlow<FeatureState<Unit>?> = _reportProblemState

    fun sendProblem(text: String) {
        viewModelScope.launch {
            _reportProblemState.value = FeatureState.Loading
            _reportProblemState.value = sendProblemUseCase(text)
        }
    }

}