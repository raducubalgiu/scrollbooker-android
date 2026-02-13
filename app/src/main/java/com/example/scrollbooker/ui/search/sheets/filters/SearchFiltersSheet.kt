package com.example.scrollbooker.ui.search.sheets.filters
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.components.core.slider.CustomSlider
import com.example.scrollbooker.core.enums.SearchSortEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.sheets.SearchSheetActions
import com.example.scrollbooker.ui.search.sheets.SearchSheetInfo
import com.example.scrollbooker.ui.search.sheets.SearchSheetsHeader
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.headlineLarge
import java.math.BigDecimal

@Composable
fun SearchFiltersSheet(
    viewModel: SearchViewModel,
    onClose: () -> Unit,
    onFilter: (SearchFiltersSheetState) -> Unit
) {
    val verticalScroll = rememberScrollState()

    val requestState by viewModel.request.collectAsState()
    val filters = requestState.filters

    var sheetState by rememberSaveable(filters) {
        mutableStateOf(
            SearchFiltersSheetState(
                maxPrice = filters.maxPrice,
                sort = filters.sort,
                hasDiscount = filters.hasDiscount
            )
        )
    }

    val isConfirmEnabled = sheetState.hasChangesComparedTo(filters)
    val isClearEnabled = listOf(
        sheetState.hasDiscount,
        sheetState.maxPrice != BigDecimal(1500),
        sheetState.sort != SearchSortEnum.RECOMMENDED
    ).count { it } > 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        SearchSheetsHeader(
            title = "",
            onClose = onClose
        )

        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            style = headlineLarge,
            color = OnBackground,
            fontWeight = FontWeight.ExtraBold,
            text = stringResource(R.string.filters)
        )

        Spacer(Modifier.height(BasePadding))

        Column(modifier = Modifier
            .weight(1f)
            .verticalScroll(verticalScroll)
        ) {
            SearchSheetInfo(
                leftText = stringResource(R.string.options),
                rightText = ""
            )

            MainButtonOutlined(
                modifier = Modifier.padding(
                    top = BasePadding,
                    start = BasePadding,
                    end = BasePadding
                ),
                icon = painterResource(R.drawable.ic_percent_badge_outline),
                onClick = { sheetState = sheetState.copy(hasDiscount = !sheetState.hasDiscount) },
                title = stringResource(R.string.sale),
                border = BorderStroke(
                    width = if(sheetState.hasDiscount) 2.dp else 1.dp,
                    color = if(sheetState.hasDiscount) Primary else Divider
                )
            )

            Spacer(Modifier.height(BasePadding))

            SearchSheetInfo(
                leftText = stringResource(R.string.maximumPrice),
                rightText = "${sheetState.maxPrice?.toInt() ?: 1500f} RON"
            )

            CustomSlider(
                modifier = Modifier.padding(SpacingXL),
                value = sheetState.maxPrice?.toFloat() ?: 1500f,
                onValueChange = {
                    sheetState = sheetState.copy(maxPrice = it.toBigDecimal())
                },
                valueRange = 0f..1500f
            )

            Spacer(Modifier.height(BasePadding))

            SearchSheetInfo(
                leftText = stringResource(R.string.sortBy),
                rightText = ""
            )

            SearchSortEnum.entries.forEach { option ->
                InputRadio(
                    selected = sheetState.sort == option,
                    onSelect = { sheetState = sheetState.copy(sort = option) },
                    headLine = stringResource(option.labelRes),
                    paddingHorizontal = BasePadding
                )
            }
        }

        Column {
            HorizontalDivider(color = Divider, thickness = 0.55.dp)

            SearchSheetActions(
                onClear = { sheetState = sheetState.clear(BigDecimal(1500)) },
                onConfirm = { onFilter(sheetState) },
                isConfirmEnabled = isConfirmEnabled,
                isClearEnabled = isClearEnabled
            )
        }
    }
}