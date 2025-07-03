package com.example.scrollbooker.screens.profile.myBusiness.myBusinessLocation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.businessType.domain.model.BusinessType
import com.example.scrollbooker.entity.businessType.domain.useCase.GetAllPaginatedBusinessTypesUseCase
import com.example.scrollbooker.entity.mapbbox.domain.model.Address
import com.example.scrollbooker.entity.mapbbox.domain.useCase.SearchAddressUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.model.SearchUsernameResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyBusinessLocationViewModel @Inject constructor(
    private val getAllBusinessTypesUseCase: GetAllPaginatedBusinessTypesUseCase,
    private val searchAddressUseCase: SearchAddressUseCase
): ViewModel() {
    private val _businessTypes: Flow<PagingData<BusinessType>> by lazy {
        getAllBusinessTypesUseCase().cachedIn(viewModelScope)
    }
    val businessTypes: Flow<PagingData<BusinessType>> get() = _businessTypes

    private val _selectedBusinessType = MutableStateFlow<BusinessType?>(null)
    val selectedBusinessType: StateFlow<BusinessType?> = _selectedBusinessType

    fun setBusinessType(businessType: BusinessType) {
        _selectedBusinessType.value = businessType
    }

    private val _searchState = MutableStateFlow<FeatureState<List<Address>>?>(null)
    val searchState: StateFlow<FeatureState<List<Address>>?> = _searchState

    private val _currentAddress = MutableStateFlow("")
    val currentAddress: StateFlow<String> = _currentAddress

    private var debounceJob: Job? = null

    fun searchAddress(query: String) {
        _currentAddress.value = query

        if(query.length < 3) {
            debounceJob?.cancel()
            _searchState.value = null
            return
        }

        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(300)

            val latest = currentAddress.value
            if(latest.length < 3 || latest != query) return@launch

            _searchState.value = FeatureState.Loading

            _searchState.value = withVisibleLoading {
                searchAddressUseCase(query)
            }
        }
    }
}