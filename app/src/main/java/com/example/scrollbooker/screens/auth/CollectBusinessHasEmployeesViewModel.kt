package com.example.scrollbooker.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.business.domain.model.Business
import com.example.scrollbooker.entity.business.domain.useCase.GetBusinessByUserUseCase
import com.example.scrollbooker.entity.business.domain.useCase.UpdateBusinessHasEmployeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CollectBusinessHasEmployeesViewModel @Inject constructor(
    private val getBusinessByUserUseCase: GetBusinessByUserUseCase,
    private val updateBusinessHasEmployeesUseCase: UpdateBusinessHasEmployeesUseCase
): ViewModel() {

    private val _businessState = MutableStateFlow<FeatureState<Business>>(FeatureState.Loading)
    val businessState: StateFlow<FeatureState<Business>> = _businessState

    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    init {
        loadUserBusiness()
    }

    fun loadUserBusiness() {
        viewModelScope.launch {
            _businessState.value = FeatureState.Loading
            _businessState.value = withVisibleLoading {
                getBusinessByUserUseCase()
            }
        }
    }

    fun updateBusinessHasEmployees(hasEmployees: Boolean) {
        val currentBusiness = (_businessState.value as? FeatureState.Success)?.data ?: return
        val updatedBusiness = currentBusiness.copy(hasEmployees = hasEmployees)
        _businessState.value = FeatureState.Success(updatedBusiness)
    }

    suspend fun updateHasEmployees(): AuthState? {
        _isSaving.value = FeatureState.Loading

        val business = (_businessState.value as? FeatureState.Success)?.data

        return if(business?.hasEmployees != null) {
            val response = withVisibleLoading { updateBusinessHasEmployeesUseCase(business.hasEmployees) }

            response
                .onFailure { error ->
                    _isSaving.value = FeatureState.Error(error)
                    Timber.Forest.tag("Business Has Employees").e("ERROR: on Updating Business Has Employees $error")
                }
                .onSuccess { updated ->
                    _isSaving.value = FeatureState.Success(Unit)
                }
                .getOrNull()
        } else {
            null
        }
    }
}