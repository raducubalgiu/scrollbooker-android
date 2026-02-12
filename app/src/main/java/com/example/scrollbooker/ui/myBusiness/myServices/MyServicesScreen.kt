package com.example.scrollbooker.screens.auth.collectBusinessDetails.collectBusinessServices
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.accordion.Accordion
import com.example.scrollbooker.components.core.inputs.InputCheckbox
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.snackbar.CustomSnackBar
import com.example.scrollbooker.core.snackbar.rememberSnackBarController
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.myBusiness.myServices.MyServicesViewModel
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun MyServicesScreen(
    viewModel: MyServicesViewModel,
    buttonTitle: String,
    onBack: () -> Unit,
    onNextOrSave: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    val defaultSelectedIds by viewModel.defaultSelectedServiceIds.collectAsState()
    val selectedIds by viewModel.selectedServiceIds.collectAsState()

    val isLoading = isSaving is FeatureState.Loading
    val isEnabled = !isLoading && selectedIds.isNotEmpty() && selectedIds != defaultSelectedIds

    val hostState = remember { SnackbarHostState() }
    val snackBarController = rememberSnackBarController(hostState)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            snackBarController.show(event)
        }
    }

    FormLayout(
        headLine = stringResource(id = R.string.services),
        subHeadLine = stringResource(id = R.string.addYourBusinessServices),
        buttonTitle = buttonTitle,
        onBack = onBack,
        onNext = onNextOrSave,
        isEnabled = isEnabled,
        isLoading = isLoading,
        snackBarHost = {
            CustomSnackBar(
                hostState = hostState,
                type = snackBarController.currentType
            )
        }
    ) {
        when (val result = state) {
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Success -> {
                LazyColumn {
                    items(result.data) {
                        var isExpanded by rememberSaveable { mutableStateOf(true) }

                        Accordion(
                            modifier = Modifier.padding(
                                start = BasePadding,
                                end = BasePadding,
                                bottom = BasePadding
                            ),
                            title = it.name,
                            isExpanded = isExpanded,
                            onSetExpanded = { isExpanded = !isExpanded }
                        ) {
                            it.services.forEachIndexed { index, service ->
                                InputCheckbox(
                                    height = 60.dp,
                                    checked = service.id in selectedIds,
                                    onCheckedChange = { viewModel.toggleService(service.id) },
                                    headLine = service.name
                                )

                                if(index < result.data.lastIndex) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = SpacingXXL)
                                            .height(0.55.dp)
                                            .background(Divider.copy(alpha = 0.5f))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}