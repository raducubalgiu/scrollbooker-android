package com.example.scrollbooker.ui.search.sheets.filters
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.sheets.SearchSheetActions
import com.example.scrollbooker.ui.search.sheets.SearchSheetInfo
import com.example.scrollbooker.ui.search.sheets.SearchSheetsHeader
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineLarge
import com.example.scrollbooker.ui.theme.titleLarge

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
                maxPrice = filters.maxPrice?.toFloat(),
                sort = filters.sort,
                hasVideo = filters.hasVideo,
                hasDiscount = filters.hasDiscount,
                isLastMinute = filters.isLastMinute
            )
        )
    }

    val isConfirmEnabled = sheetState.hasChangesComparedTo(filters)

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

        Spacer(Modifier.height(SpacingXXL))

        Column(modifier = Modifier
            .weight(1f)
            .verticalScroll(verticalScroll)
        ) {
            SearchSheetInfo(
                leftText = stringResource(R.string.options),
                rightText = ""
            )

            LazyRow(
                modifier = Modifier.padding(top = SpacingM),
                contentPadding = PaddingValues(horizontal = BasePadding)
            ) {
                item {
                    MainButtonOutlined(
                        icon = painterResource(R.drawable.ic_percent_badge_outline),
                        onClick = { sheetState = sheetState.copy(hasDiscount = !sheetState.hasDiscount) },
                        title = stringResource(R.string.sale),
                        border = BorderStroke(
                            width = if(sheetState.hasDiscount) 2.dp else 1.dp,
                            color = if(sheetState.hasDiscount) Primary else Divider
                        )
                    )

                    Spacer(Modifier.width(SpacingS))

                    MainButtonOutlined(
                        icon = painterResource(R.drawable.ic_bolt_outline),
                        onClick = { sheetState = sheetState.copy(isLastMinute = !sheetState.isLastMinute) },
                        title = stringResource(R.string.lastMinute),
                        border = BorderStroke(
                            width = if(sheetState.isLastMinute) 2.dp else 1.dp,
                            color = if(sheetState.isLastMinute) Primary else Divider
                        )
                    )
                }
            }

            Spacer(Modifier.height(SpacingXXL))

            SearchSheetInfo(
                leftText = stringResource(R.string.maximumPrice),
                rightText = "${sheetState.maxPrice ?: 1500f} RON"
            )

            CustomSlider(
                modifier = Modifier.padding(SpacingXL),
                value = sheetState.maxPrice ?: 1500f,
                onValueChange = { sheetState = sheetState.copy(maxPrice = it) },
                valueRange = 0f..1500f
            )

            Spacer(Modifier.height(BasePadding))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BasePadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.video),
                        fontWeight = FontWeight.SemiBold,
                        style = titleLarge
                    )
                    Spacer(Modifier.height(SpacingXS))
                    Text(
                        style = bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        text = stringResource(R.string.searchVideoDescription),
                    )
                }

                Spacer(Modifier.width(SpacingS))

                Switch(
                    checked = sheetState.hasVideo,
                    onCheckedChange = { sheetState = sheetState.copy(hasVideo = it) },
                    colors = SwitchDefaults.colors(
                        uncheckedBorderColor = Divider,
                        uncheckedTrackColor = Divider,
                        uncheckedThumbColor = Background
                    )
                )
            }

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
                onClear = { sheetState = sheetState.clear() },
                onConfirm = { onFilter(sheetState) },
                isConfirmEnabled = isConfirmEnabled
            )
        }
    }
}