package com.example.scrollbooker.screens.auth.collectBusinessDetails.collectBusinessServices

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.LoadingScreen
import com.example.scrollbooker.screens.profile.myBusiness.myServices.MyServicesViewModel
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun MyServicesScreen(
    viewModel: MyServicesViewModel,
    onBack: () -> Unit,
    onNextOrSave: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val selectedIds by viewModel.selectedServiceIds.collectAsState()

    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
    ) {
        FormLayout(
            headLine = stringResource(id = R.string.services),
            subHeadLine = stringResource(id = R.string.addYourBusinessServices),
            buttonTitle = stringResource(id = R.string.nextStep),
            onBack = onBack,
            onNext = onNextOrSave,
        ) {
            when (val result = state) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Success -> {
                    LazyColumn {
                        itemsIndexed(result.data) { index, service ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .background(Background)
                                    .clickable { viewModel.toggleService(service.id) },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    modifier = Modifier.padding(start = SpacingXXL),
                                    text = service.name,
                                    style = bodyLarge,
                                    color = OnBackground
                                )
                                Checkbox(
                                    modifier = Modifier.padding(end = SpacingXXL),
                                    checked = service.id in selectedIds,
                                    onCheckedChange = null,
                                    colors = CheckboxColors(
                                        checkedCheckmarkColor = Color.White,
                                        uncheckedCheckmarkColor = Color.Transparent,
                                        checkedBoxColor = Primary,
                                        uncheckedBoxColor = Color.Transparent,
                                        disabledCheckedBoxColor = Divider,
                                        disabledUncheckedBoxColor = Divider,
                                        disabledIndeterminateBoxColor = Divider,
                                        checkedBorderColor = Primary,
                                        uncheckedBorderColor = Color.Gray,
                                        disabledBorderColor = Divider,
                                        disabledUncheckedBorderColor = Divider,
                                        disabledIndeterminateBorderColor = Divider
                                    )
                                )
                            }

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