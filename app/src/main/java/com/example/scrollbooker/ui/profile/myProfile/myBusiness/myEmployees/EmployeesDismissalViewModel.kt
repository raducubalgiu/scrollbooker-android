package com.example.scrollbooker.ui.profile.myProfile.myBusiness.myEmployees

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class EmployeesDismissalViewModel @Inject constructor(
//    private val getEmployeeDismissalUseCase: GetEmployeeDismissalUseCase,
//    savedStateHandle: SavedStateHandle
//): ViewModel() {
//
//    private val _employeeDismissalState = MutableStateFlow<FeatureState<EmployeeDismissal>>(
//        FeatureState.Loading)
//    val employeeDismissalState: StateFlow<FeatureState<EmployeeDismissal>> = _employeeDismissalState
//
//    init {
//        loadEmployeeDismissalConsent()
//    }
//
//    fun loadEmployeeDismissalConsent() {
//        viewModelScope.launch {
//            _employeeDismissalState.value = FeatureState.Loading
//            _employeeDismissalState.value = getEmployeeDismissalUseCase()
//        }
//    }
//}