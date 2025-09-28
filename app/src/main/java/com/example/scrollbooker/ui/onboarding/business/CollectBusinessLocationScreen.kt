package com.example.scrollbooker.ui.onboarding.business

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.components.core.inputs.SearchBar
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.myBusiness.myBusinessLocation.MyBusinessLocationViewModel
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun CollectBusinessLocationScreen(
    viewModel: MyBusinessLocationViewModel,
    onBack: () -> Unit,
    onNextOrSave: () -> Unit,
) {
    val currentQuery by viewModel.currentQuery.collectAsState()
    val searchState by viewModel.searchState.collectAsState()
    val selectedAddress by viewModel.selectedBusinessAddress.collectAsState()

    FormLayout(
        headLine = stringResource(id = R.string.locationAddress),
        subHeadLine = stringResource(id = R.string.addYourBusinessLocation),
        buttonTitle = stringResource(id = R.string.nextStep),
        isEnabled = selectedAddress != null,
        onBack = onBack,
        onNext = onNextOrSave,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(
                        start = SpacingXL,
                        end = SpacingXXL,
                        bottom = SpacingS
                    )
            ) {
                SearchBar(
                    value = currentQuery,
                    onValueChange = { viewModel.searchAddress(it) },
                    placeholder = stringResource(R.string.searchAddress)
                )
            }

            when(val state = searchState) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Success -> {
                    val results = state.data

                    if(results.isEmpty()) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = BasePadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.notFoundAnyResult),
                                color = Color.Gray
                            )
                        }
                    }

                    LazyColumn {
                        itemsIndexed(results) { index, address ->
                            InputRadio(
                                selected = selectedAddress?.placeId == address.placeId,
                                headLine = address.description,
                                onSelect = { viewModel.setBusinessAddress(address) },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_search_solid),
                                        contentDescription = null
                                    )
                                }
                            )

                            if(index < results.size - 1) {
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
                else -> Unit
            }
        }
    }
}