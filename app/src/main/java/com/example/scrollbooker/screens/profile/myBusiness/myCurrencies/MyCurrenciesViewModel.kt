package com.example.scrollbooker.screens.profile.myBusiness.myCurrencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.shared.currencies.domain.model.Currency
import com.example.scrollbooker.shared.currencies.domain.useCase.GetAllCurrenciesUseCase
import com.example.scrollbooker.shared.currencies.domain.useCase.GetUserCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCurrenciesViewModel @Inject constructor(
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
    private val getUserCurrenciesUseCase: GetUserCurrenciesUseCase
): ViewModel() {
    private val _allCurrenciesState = MutableStateFlow<FeatureState<List<Currency>>>(FeatureState.Loading)
    val allCurrenciesState: StateFlow<FeatureState<List<Currency>>> = _allCurrenciesState

    private val _userCurrenciesState = MutableStateFlow<FeatureState<List<Currency>>>(FeatureState.Loading)
    val userCurrenciesState: StateFlow<FeatureState<List<Currency>>> = _userCurrenciesState

    fun loadAllCurrencies() {
        viewModelScope.launch {
            _allCurrenciesState.value = FeatureState.Loading
            _allCurrenciesState.value = getAllCurrenciesUseCase()
        }
    }
}