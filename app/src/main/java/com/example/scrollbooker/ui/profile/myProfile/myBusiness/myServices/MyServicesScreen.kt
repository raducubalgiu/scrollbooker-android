package com.example.scrollbooker.screens.auth.collectBusinessDetails.collectBusinessServices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputCheckbox
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.ui.profile.myProfile.myBusiness.myServices.MyServicesViewModel
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

    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
    ) {
        FormLayout(
            headLine = stringResource(id = R.string.services),
            subHeadLine = stringResource(id = R.string.addYourBusinessServices),
            buttonTitle = buttonTitle,
            onBack = onBack,
            onNext = onNextOrSave,
            isEnabled = isEnabled,
            isLoading = isLoading
        ) {
            when (val result = state) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Success -> {
                    LazyColumn {
                        itemsIndexed(result.data) { index, service ->
                            InputCheckbox(
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