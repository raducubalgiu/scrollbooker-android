package com.example.scrollbooker.screens.auth.collectBusinessDetails.collectBusinessType

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.shared.businessType.domain.model.BusinessType
import com.example.scrollbooker.shared.businessType.domain.useCase.GetAllPaginatedBusinessTypesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CollectBusinessTypeViewModel @Inject constructor(
    getAllBusinessTypesUseCase: GetAllPaginatedBusinessTypesUseCase
): ViewModel() {
    private val _businessTypes: Flow<PagingData<BusinessType>> by lazy {
        getAllBusinessTypesUseCase().cachedIn(viewModelScope)
    }

    val businessTypes: Flow<PagingData<BusinessType>> get() = _businessTypes
}