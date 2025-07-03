package com.example.scrollbooker.screens.auth.collectClientDetails
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectClientLocationPermissionViewModel @Inject constructor(
    //private val locationClient: FusedLocationProviderClient,
): ViewModel() {

//    var isSubmitted by mutableStateOf(false)
//        private set
//
//    fun submitLocationUpdate(
//        hasPermission: Boolean,
//        lat: Double? = null,
//        lng: Double? = null,
//        onSuccess: () -> Unit,
//        onError: (Throwable) -> Unit
//    ) {
//        viewModelScope.launch {  }
//    }
}