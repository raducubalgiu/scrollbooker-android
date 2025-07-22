package com.example.scrollbooker.screens.profile.myProfile.myBusiness.myCurrencies
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun MyCurrenciesScreen(
    viewModel: MyCurrenciesViewModel,
    buttonTitle: String,
    onBack: () -> Unit,
    onNextOrSave: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    val defaultSelectedIds by viewModel.defaultSelectedCurrencyIds.collectAsState()
    val selectedIds by viewModel.selectedCurrencyIds.collectAsState()

    val isLoading = isSaving is FeatureState.Loading
    val isEnabled = !isLoading && selectedIds.isNotEmpty() && selectedIds != defaultSelectedIds

    FormLayout(
        modifier = Modifier.statusBarsPadding(),
        headLine = stringResource(R.string.acceptedCurrencies),
        subHeadLine = stringResource(R.string.chooseDesiredCurrencies),
        buttonTitle = buttonTitle,
        isEnabled = isEnabled,
        isLoading = isLoading,
        onBack = onBack,
        onNext = onNextOrSave
    ) {
        when (val result = state) {
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Success -> {
                Column(modifier = Modifier
                    .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        itemsIndexed(result.data) { index, currency ->
                            InputCheckbox(
                                checked = currency.id in selectedIds,
                                onCheckedChange = { viewModel.toggleCurrency(currency.id) },
                                headLine = currency.name
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