package com.example.scrollbooker.screens.auth.collectClientDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.enums.GenderTypeEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateGenderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CollectClientGenderViewModel @Inject constructor(
    private val updateGenderUseCase: UpdateGenderUseCase
): ViewModel() {
    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    suspend fun collectUserGender(gender: GenderTypeEnum): AuthState? {
        _isSaving.value = FeatureState.Loading
        delay(300)

        return updateGenderUseCase(gender.key)
            .onFailure { e ->
                _isSaving.value = FeatureState.Error(e)
                Timber.tag("Update Gender").e("ERROR: on updating User Gender $e")
            }
            .onSuccess {
                _isSaving.value = FeatureState.Success(Unit)
            }
            .getOrNull()
    }
}