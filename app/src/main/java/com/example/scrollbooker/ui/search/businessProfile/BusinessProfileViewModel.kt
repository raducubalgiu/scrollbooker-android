package com.example.scrollbooker.ui.search.businessProfile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfile
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessProfileUseCase
import com.example.scrollbooker.navigation.routes.MainRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BusinessProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBusinessProfileUseCase: GetBusinessProfileUseCase,
): ViewModel() {
    private val businessId: Int = checkNotNull(savedStateHandle[MainRoute.BusinessProfile.ARG])

    @OptIn(ExperimentalCoroutinesApi::class)
    val businessProfileState: StateFlow<FeatureState<BusinessProfile>> =
        flow {
            emit(FeatureState.Loading)
            emit(withVisibleLoading { getBusinessProfileUseCase(businessId) })
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FeatureState.Loading
            )
}