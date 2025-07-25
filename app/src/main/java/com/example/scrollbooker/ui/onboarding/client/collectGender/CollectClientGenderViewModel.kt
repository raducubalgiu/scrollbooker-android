package com.example.scrollbooker.ui.onboarding.client.collectGender

import androidx.lifecycle.ViewModel
import com.example.scrollbooker.core.enums.GenderTypeEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.onboarding.domain.useCase.CollectClientGenderUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateGenderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CollectClientGenderViewModel @Inject constructor(
    private val collectClientGenderUseCase: CollectClientGenderUseCase
): ViewModel() {
    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    suspend fun collectUserGender(gender: GenderTypeEnum): Result<AuthState> {
        _isSaving.value = FeatureState.Loading

        val result = withVisibleLoading { collectClientGenderUseCase(gender.key) }

        result
            .onFailure { e ->
                _isSaving.value = FeatureState.Error(e)
                Timber.tag("Update Gender").e("ERROR: on updating User Gender $e")
            }
            .onSuccess {
                _isSaving.value = FeatureState.Success(Unit)
            }

        return result
    }
}