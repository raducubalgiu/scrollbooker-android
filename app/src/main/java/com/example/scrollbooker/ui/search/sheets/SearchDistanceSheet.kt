package com.example.scrollbooker.ui.search.sheets
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@Composable
fun SearchDistanceSheet(
    viewModel: SearchViewModel,
    onClose: () -> Unit
) {
    val maximumDistance by viewModel.maximumDistance.collectAsState()
    val distance by viewModel.distance.collectAsState()

    SearchSheetsHeader(
        title = stringResource(R.string.distance),
        onClose = onClose
    )

    Spacer(Modifier.height(BasePadding))

    SearchSheetInfo(
        leftText = stringResource(R.string.maximumDistance),
        rightText = "${distance.toInt()} km"
    )

    CustomSlider(
        modifier = Modifier.padding(SpacingXL),
        value = distance,
        onValueChange = { viewModel.setDistance(it) },
        valueRange = 0f..maximumDistance
    )

    Spacer(Modifier.height(SpacingXL))

    SearchSheetActions(
        onClear = {
           viewModel.setDistance(maximumDistance)
        },
        onConfirm = {
            //viewModel.setDistance()
        }
    )
}