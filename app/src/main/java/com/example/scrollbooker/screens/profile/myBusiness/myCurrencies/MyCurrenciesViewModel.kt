package com.example.scrollbooker.screens.profile.myBusiness.myCurrencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.shared.currencies.domain.useCase.GetAllCurrenciesUseCase
import com.example.scrollbooker.shared.currencies.domain.useCase.GetUserCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyCurrenciesViewModel @Inject constructor(
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
    private val getUserCurrenciesUseCase: GetUserCurrenciesUseCase
): ViewModel() {
    private val _state = MutableStateFlow<FeatureState<List<CurrencyUiModel>>>(FeatureState.Loading)
    val state: StateFlow<FeatureState<List<CurrencyUiModel>>> = _state

    private var initialSelectedIds: Set<Int> = emptySet()

    fun fetchCurrencies() {
        viewModelScope.launch {
            _state.value = FeatureState.Loading

            val all = getAllCurrenciesUseCase()
            val user = getUserCurrenciesUseCase()

            if(all.isSuccess && user.isSuccess) {
                val selectedIds = user.getOrThrow().map { it.id }.toSet()
                initialSelectedIds = selectedIds

                val combined = all.getOrThrow().map {
                    CurrencyUiModel(
                        id = it.id,
                        name = it.name,
                        isSelected = it.id in selectedIds,
                    )
                }

                _state.value = FeatureState.Success(combined)
            } else {
                val error = all.exceptionOrNull() ?: user.exceptionOrNull()
                Timber.tag("Currencies").e("ERROR: on Fetching Currencies $error")
                _state.value = FeatureState.Error(error ?: Exception("Unexpected Error"))
            }
        }
    }
}