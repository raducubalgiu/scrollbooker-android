package com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.flow.assignJob

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.profession.domain.model.Profession
import com.example.scrollbooker.entity.profession.domain.useCase.GetProfessionsByBusinessTypeUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmploymentAssignJobViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getProfessionsByBusinessTypeUseCase: GetProfessionsByBusinessTypeUseCase
): ViewModel() {
    private val _professionsState = MutableStateFlow<FeatureState<List<Profession>>>(FeatureState.Loading)
    val professionsState: StateFlow<FeatureState<List<Profession>>> = _professionsState

    init {
        getProfessions()
    }

    private fun getProfessions() {
        viewModelScope.launch {
            _professionsState.value = FeatureState.Loading

            val businessTypeId = authDataStore.getBusinessTypeId().firstOrNull()
            _professionsState.value = getProfessionsByBusinessTypeUseCase(businessTypeId)
        }
    }
}