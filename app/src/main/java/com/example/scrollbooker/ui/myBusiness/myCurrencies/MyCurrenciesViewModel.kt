package com.example.scrollbooker.ui.myBusiness.myCurrencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import com.example.scrollbooker.entity.nomenclature.currency.domain.useCase.GetAllCurrenciesUseCase
import com.example.scrollbooker.entity.nomenclature.currency.domain.useCase.GetUserCurrenciesUseCase
import com.example.scrollbooker.entity.nomenclature.currency.domain.useCase.UpdateUserCurrenciesUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyCurrenciesViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
    private val getUserCurrenciesUseCase: GetUserCurrenciesUseCase,
    private val updateUserCurrenciesUseCase: UpdateUserCurrenciesUseCase
): ViewModel() {
    private val _state = MutableStateFlow<FeatureState<List<Currency>>>(FeatureState.Loading)
    val state: StateFlow<FeatureState<List<Currency>>> = _state

    private val _defaultSelectedCurrencyIds = MutableStateFlow<Set<Int>>(emptySet())
    val defaultSelectedCurrencyIds: StateFlow<Set<Int>> = _defaultSelectedCurrencyIds.asStateFlow()

    private val _selectedCurrencyIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedCurrencyIds: StateFlow<Set<Int>> = _selectedCurrencyIds.asStateFlow()

    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    init { fetchCurrencies() }

    fun fetchCurrencies() {
        viewModelScope.launch {
            _state.value = FeatureState.Loading

            val userId = authDataStore.getUserId().firstOrNull()

            if(userId == null) {
                throw IllegalStateException("User Id not found in AuthDataStore")
            }

            val result = withVisibleLoading {
                val all = getAllCurrenciesUseCase()
                val user = getUserCurrenciesUseCase(userId)

                if(all.isSuccess && user.isSuccess) {
                    val selectedIds = user.getOrThrow().map { it.id }.toSet()

                    _selectedCurrencyIds.value = selectedIds
                    _defaultSelectedCurrencyIds.value = selectedIds

                    val combined = all.getOrThrow().map {
                        Currency(
                            id = it.id,
                            name = it.name
                        )
                    }

                    return@withVisibleLoading FeatureState.Success(combined)
                } else {
                    val error = all.exceptionOrNull() ?: user.exceptionOrNull()
                    Timber.tag("Currencies").e("ERROR: on Fetching Currencies $error")
                    return@withVisibleLoading FeatureState.Error(error ?: Exception("Unexpected Error"))
                }
            }

            _state.value = result
        }
    }

    fun toggleCurrency(currencyId: Int) {
        _selectedCurrencyIds.update { current ->
            if(currencyId in current) current - currencyId else current + currencyId
        }
    }

    suspend fun updateBusinessServices(): AuthState? {
        _isSaving.value = FeatureState.Loading

        val currencyIds = _selectedCurrencyIds.value.toList()
        val result = withVisibleLoading { updateUserCurrenciesUseCase(currencyIds) }

        return result
            .onFailure { e ->
                _isSaving.value = FeatureState.Error(e)
                Timber.tag("Update Currencies").e("ERROR: on updating User Currencies $e")
            }
            .onSuccess {
                _isSaving.value = FeatureState.Success(Unit)
            }
            .getOrNull()
    }
}