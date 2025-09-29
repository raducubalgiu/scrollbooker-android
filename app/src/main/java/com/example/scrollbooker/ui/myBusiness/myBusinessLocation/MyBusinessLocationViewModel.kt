package com.example.scrollbooker.ui.myBusiness.myBusinessLocation
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.VideoPlayerCache
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessAddress
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessCreateResponse
import com.example.scrollbooker.entity.booking.business.domain.useCase.CreateBusinessUseCase
import com.example.scrollbooker.entity.booking.business.domain.useCase.SearchBusinessAddressUseCase
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.example.scrollbooker.entity.nomenclature.businessType.domain.useCase.GetAllPaginatedBusinessTypesUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class BusinessPhotoUIState(
    val images: List<Uri?> = List(5) { null }
)

@HiltViewModel
class MyBusinessLocationViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    private val authDataStore: AuthDataStore,
    private val getAllBusinessTypesUseCase: GetAllPaginatedBusinessTypesUseCase,
    private val searchBusinessAddressUseCase: SearchBusinessAddressUseCase,
    private val createBusinessUseCase: CreateBusinessUseCase
): ViewModel() {
    private val _photosState = MutableStateFlow(BusinessPhotoUIState())
    val photosState: StateFlow<BusinessPhotoUIState> = _photosState

    private val _videoState = MutableStateFlow<Uri?>(null)
    val videoState: StateFlow<Uri?> = _videoState

    val player: ExoPlayer = ExoPlayer.Builder(application.applicationContext)
        .build().apply {
            repeatMode = Player.REPEAT_MODE_OFF
            playWhenReady = true
        }

    private val _businessTypes: Flow<PagingData<BusinessType>> by lazy {
        getAllBusinessTypesUseCase().cachedIn(viewModelScope)
    }
    val businessTypes: Flow<PagingData<BusinessType>> get() = _businessTypes

    private val _selectedBusinessType = MutableStateFlow<BusinessType?>(null)
    val selectedBusinessType: StateFlow<BusinessType?> = _selectedBusinessType

    fun setBusinessType(businessType: BusinessType) {
        _selectedBusinessType.value = businessType
    }

    private val _searchState = MutableStateFlow<FeatureState<List<BusinessAddress>>?>(null)
    val searchState: StateFlow<FeatureState<List<BusinessAddress>>?> = _searchState

    private val _selectedAddress = MutableStateFlow<BusinessAddress?>(null)
    val selectedBusinessAddress: StateFlow<BusinessAddress?> = _selectedAddress

    private val _currentQuery = MutableStateFlow("")
    val currentQuery: StateFlow<String> = _currentQuery

    fun setBusinessAddress(businessAddress: BusinessAddress) {
        _selectedAddress.value = businessAddress
    }

    private val _currentName = MutableStateFlow("")
    val currentName: StateFlow<String> = _currentName

    fun setBusinessName(businessName: String) {
        _currentName.value = businessName
    }

    private val _currentDescription = MutableStateFlow("")
    val currentDescription: StateFlow<String> = _currentDescription

    fun setBusinessDescription(businessDescription: String) {
        _currentDescription.value = businessDescription
    }

    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    private var debounceJob: Job? = null

    fun searchAddress(query: String) {
        _currentQuery.value = query

        if(query.length < 3) {
            debounceJob?.cancel()
            _searchState.value = null
            return
        }

        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(200)

            val latest = currentQuery.value
            if(latest.length < 3 || latest != query) return@launch

            _searchState.value = FeatureState.Loading

            _searchState.value = withVisibleLoading {
                searchBusinessAddressUseCase(query)
            }
        }
    }

    fun setImage(slot: Int, uri: Uri?) {
        if(slot !in 0..4) return
        _photosState.update { s ->
            s.copy(images = s.images.toMutableList().also { it[slot] = uri })
        }
    }

    fun setVideo(uri: Uri?) {
        if(uri != null) {
            _videoState.value = uri
        }
    }

    fun clearImage(slot: Int) = setImage(slot, null)
    fun clearVideo() {
        _videoState.value = null
    }

    suspend fun createBusiness(): BusinessCreateResponse? {
        _isSaving.value = FeatureState.Loading
        val placeId = _selectedAddress.value?.placeId
        val businessTypeId = _selectedBusinessType.value?.id

        if(placeId.isNullOrEmpty() || businessTypeId == null) {
            Timber.tag("Create Business").e("Place Id or Business Type Id is null")
            return null
        }

        val result = withVisibleLoading {
            createBusinessUseCase(
                description = _currentDescription.value,
                placeId = placeId,
                businessTypeId = businessTypeId,
                ownerFullName = _currentName.value
            )
        }

        return result
            .onFailure { error ->
                _isSaving.value = FeatureState.Error(error)
                Timber.Forest.tag("Create Business").e("ERROR: on creating Business $error")
            }
            .onSuccess { response ->
                _isSaving.value = FeatureState.Success(Unit)

                authDataStore.setBusinessId(response.businessId)
                authDataStore.setBusinessTypeId(businessTypeId)
            }
            .getOrNull()
    }
}