package com.example.scrollbooker.ui.search.businessProfile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfile
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BusinessProfileViewModel @Inject constructor(
    private val getBusinessProfileUseCase: GetBusinessProfileUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val businessIdFlow: StateFlow<Int?> = savedStateHandle.getStateFlow("businessId", null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val businessProfileState: StateFlow<FeatureState<BusinessProfile>> =
        businessIdFlow
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { id ->
                flow {
                    emit(FeatureState.Loading)
                    emit(withVisibleLoading { getBusinessProfileUseCase(id) })
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FeatureState.Loading
            )

}