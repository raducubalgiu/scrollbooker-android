package com.example.scrollbooker.ui.onboarding.shared
import androidx.lifecycle.ViewModel
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.onboarding.domain.useCase.CollectUserLocationPermissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CollectClientLocationPermissionViewModel @Inject constructor(
    private val collectUserLocationPermissionUseCase: CollectUserLocationPermissionUseCase
): ViewModel() {
    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    suspend fun collectLocationPermission(): Result<AuthState> {
        _isSaving.value = FeatureState.Loading

        val result = withVisibleLoading {
            collectUserLocationPermissionUseCase()
        }

        result
            .onFailure {
                _isSaving.value = FeatureState.Error()
            }
            .onSuccess {
                _isSaving.value = FeatureState.Success(Unit)
            }

        return result
    }
}