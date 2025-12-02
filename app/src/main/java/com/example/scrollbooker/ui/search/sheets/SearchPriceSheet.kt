package com.example.scrollbooker.ui.search.sheets
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.slider.CustomSlider
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.search.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPriceSheet(
    viewModel: SearchViewModel,
    onClose: () -> Unit
) {
//    val maximumPrice by viewModel.maximumPrice.collectAsState()
//    val price by viewModel.price.collectAsState()

    SearchSheetsHeader(
        title = stringResource(R.string.price),
        onClose = onClose
    )

    Spacer(Modifier.height(BasePadding))

    SearchSheetInfo(
        leftText = stringResource(R.string.maximumPrice),
        rightText = "0 RON"
    )

    CustomSlider(
        modifier = Modifier.padding(SpacingXL),
        value = 0f,
        onValueChange = {  },
        valueRange = 0f..100f
    )

    Spacer(Modifier.height(SpacingXL))

    SearchSheetActions(
        onClear = {  },
        onConfirm = {}
    )
}