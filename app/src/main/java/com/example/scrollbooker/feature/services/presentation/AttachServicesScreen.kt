package com.example.scrollbooker.feature.services.presentation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.MainButton
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.core.util.FeatureState
import androidx.compose.runtime.getValue

@Composable
fun AttachServicesScreen(
    viewModel: MyServicesViewModel,
    onBack: () -> Unit
) {
    val availableServicesState by viewModel.availableServicesState.collectAsState()
    val servicesState by viewModel.servicesState.collectAsState()
    val actionState by viewModel.actionState.collectAsState()

    val serviceIds = mutableListOf<Int>()

    Layout(
        headerTitle = stringResource(R.string.addServices),
        onBack = onBack
    ) {
        Column(modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
//            when(availableServicesState) {
//                is FeatureState.Loading -> CircularProgressIndicator()
//                is FeatureState.Error -> SnackbarManager.showToast(stringResource(id = R.string.somethingWentWrong))
//                is FeatureState.Success -> {
//                    val currentServicesIds = when(servicesState) {
//                        is FeatureState.Success -> (servicesState as FeatureState.Success).data.map { it.id }.toSet()
//                        else -> emptySet()
//                    }
//
//                    val availableServicesState = (availableServicesState as FeatureState.Success<List<Service>>).data
//
//                    if(availableServicesState.isEmpty()) {
//                        Text(text = stringResource(R.string.noServicesFound))
//                    } else {
//                        LazyColumn {
//                            items(availableServicesState) { service ->
//                                var checked by remember { mutableStateOf(currentServicesIds.contains(service.id)) }
//
//                                InputCheckbox(
//                                    checked = checked,
//                                    onCheckedChange = {
//                                        checked = it
//
//                                        if(it) serviceIds.add(service.id) else serviceIds.remove(service.id)
//                                    },
//                                    isEnabled = !currentServicesIds.contains(service.id),
//                                    headLine = service.name,
//                                )
//                            }
//                        }
//                    }
//                }
//            }

            MainButton(
                title = stringResource(R.string.save),
                onClick = {
                    viewModel.attachManyServices(serviceIds)
                    onBack()
                },
                isLoading = actionState is FeatureState.Loading
            )
        }
    }
}