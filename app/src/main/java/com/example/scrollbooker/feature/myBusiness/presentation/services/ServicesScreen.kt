package com.example.scrollbooker.feature.myBusiness.presentation.services
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.feature.myBusiness.presentation.components.ServicesList
import androidx.compose.runtime.getValue
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.core.snackbar.SnackbarManager
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.myBusiness.domain.model.Service

@Composable
fun ServicesScreen(
    viewModel: ServicesViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.servicesState.collectAsState()

    Layout(
        headerTitle = stringResource(R.string.myServices),
        onBack = onBack
    ) {
        when(state) {
            is FeatureState.Loading -> CircularProgressIndicator()
            is FeatureState.Error -> SnackbarManager.showToast(stringResource(id = R.string.somethingWentWrong))
            is FeatureState.Success -> {
                val services = (state as FeatureState.Success<List<Service>>).data
                if(services.isEmpty()) {
                    Text(text = "No Services available")
                } else {
                    ServicesList(services)
                }
            }
        }
    }
}