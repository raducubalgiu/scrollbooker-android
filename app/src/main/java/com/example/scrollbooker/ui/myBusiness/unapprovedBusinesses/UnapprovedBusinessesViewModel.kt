package com.example.scrollbooker.ui.myBusiness.unapprovedBusinesses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.snackbar.SnackBarUiEvent
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.business.domain.model.UnapprovedBusiness
import com.example.scrollbooker.entity.booking.business.domain.useCase.ApproveBusinessUseCase
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetUnapprovedBusinessesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UnapprovedBusinessesViewModel @Inject constructor(
    private val getUnapprovedBusinessesUseCase: GetUnapprovedBusinessesUseCase,
    private val approveBusinessUseCase: ApproveBusinessUseCase
): ViewModel() {
    private val _unapprovedBusinessesTrigger = MutableStateFlow(0)

    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    private val _events = MutableSharedFlow<SnackBarUiEvent.Show>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val unapprovedBusinesses: Flow<PagingData<UnapprovedBusiness>> = _unapprovedBusinessesTrigger
        .flatMapLatest { getUnapprovedBusinessesUseCase() }
        .cachedIn(viewModelScope)

    fun refresh() {
        _unapprovedBusinessesTrigger.value += 1
    }

    fun approveBusiness(userId: Int) {
        viewModelScope.launch {
            _isSaving.value = FeatureState.Loading

            val result = withVisibleLoading {
                approveBusinessUseCase(userId)
            }

            result
                .onSuccess {
                    _isSaving.value = FeatureState.Success(Unit)
                    refresh()
                }
                .onFailure { error ->
                    Timber.tag("Approve Business").e(error, "ERROR: on approving business for: $userId")
                    _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
                    _isSaving.value = FeatureState.Error(error)
                }
        }
    }
}