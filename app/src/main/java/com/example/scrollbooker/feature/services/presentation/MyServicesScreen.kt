package com.example.scrollbooker.feature.services.presentation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.dialog.DialogConfirm
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.snackbar.SnackbarManager
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.services.domain.model.Service
import com.example.scrollbooker.feature.services.presentation.components.ServicesList

@Composable
fun MyServicesScreen(
    viewModel: MyServicesViewModel,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val servicesState by viewModel.servicesState.collectAsState()
    var selectedServiceId by remember { mutableStateOf("") }
    var isOpen by remember { mutableStateOf(false) }

    Layout(
        headerTitle = stringResource(R.string.myServices),
        onBack = onBack
    ) {
        if(isOpen) {
            DialogConfirm(
                title = "",
                text = stringResource(R.string.areYouSureYouWantDeleteService),
                onDismissRequest = {
                    selectedServiceId = ""
                    isOpen = false
                },
                onConfirmation = {
                    viewModel.detachServices(selectedServiceId.toInt())
                    isOpen = false
                }
            )
        }

        Column(modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            when(servicesState) {
                is FeatureState.Loading -> CircularProgressIndicator()
                is FeatureState.Error -> SnackbarManager.showToast(stringResource(id = R.string.somethingWentWrong))
                is FeatureState.Success -> {
                    val services = (servicesState as FeatureState.Success<List<Service>>).data

                    if(services.isEmpty()) {
                        Text(text = "No Services available")
                    } else {
                        ServicesList(
                            services=services,
                            selectedServiceId = selectedServiceId,
                            onSelect = {
                                selectedServiceId = it
                                isOpen = true
                            }
                        )
                    }
                }
            }

            MainButton(
                title = stringResource(R.string.add),
                onClick = { onNavigate(MainRoute.AttachServices.route) }
            )
        }
    }
}