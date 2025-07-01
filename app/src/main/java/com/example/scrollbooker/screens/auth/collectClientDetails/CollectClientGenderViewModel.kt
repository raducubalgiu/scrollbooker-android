package com.example.scrollbooker.screens.auth.collectClientDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.enums.GenderTypeEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateGenderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CollectClientGenderViewModel @Inject constructor(
    private val updateGenderUseCase: UpdateGenderUseCase
): ViewModel() {
    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    private val _navigateToNextStep = MutableStateFlow(false)
    val navigateToNextStep: StateFlow<Boolean> = _navigateToNextStep

    fun collectUserGender(gender: GenderTypeEnum) {
        viewModelScope.launch {
            _isSaving.value = FeatureState.Loading
            delay(300)

            updateGenderUseCase(gender.key)
                .onFailure { e ->
                    _isSaving.value = FeatureState.Error(e)
                    _navigateToNextStep.value = false
                    Timber.tag("Update Gender").e("ERROR: on updating User Gender $e")
                }
                .onSuccess {
                    _isSaving.value = FeatureState.Success(Unit)
                    _navigateToNextStep.value = true
                }
        }
    }
}