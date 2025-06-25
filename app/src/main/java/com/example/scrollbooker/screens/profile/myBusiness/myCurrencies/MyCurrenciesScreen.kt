package com.example.scrollbooker.screens.profile.myBusiness.myCurrencies
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.inputs.InputCheckbox
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineLarge

@Composable
fun MyCurrenciesScreen(
    viewModel: MyCurrenciesViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCurrencies()
    }

    Layout(
        modifier = Modifier.statusBarsPadding(),
        headerTitle = "",
        onBack = onBack
    ) {
        Text(
            style = headlineLarge,
            color = OnBackground,
            fontWeight = FontWeight.ExtraBold,
            text = stringResource(R.string.paymentMethods)
        )
        Text(
            text = "Alege valutele prin care clientii",
            style = bodyLarge,
            color = Color.Gray
        )

        Spacer(Modifier.height(SpacingXXL))

        when (val result = state) {
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Success -> {
                Column(modifier = Modifier
                    .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(result.data) { currency ->
                            InputCheckbox(
                                checked = currency.isSelected,
                                onCheckedChange = {},
                                headLine = currency.name
                            )
                        }
                    }
                    MainButton(
                        title = stringResource(R.string.save),
                        onClick = {}
                    )
                }
            }
        }
    }
}