package com.example.scrollbooker.ui.search.sheets.filters
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.sheets.SearchSheetActions
import com.example.scrollbooker.ui.search.sheets.SearchSheetInfo
import com.example.scrollbooker.ui.search.sheets.SearchSheetsHeader
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.headlineLarge

@Composable
fun SearchFiltersSheet(
    viewModel: SearchViewModel,
    onClose: () -> Unit,
    onClear: () -> Unit,
    onFilter: (Float?, SearchSortEnum) -> Unit
) {
    val verticalScroll = rememberScrollState()

    val requestState by viewModel.request.collectAsState()
    val filters = requestState.filters

    var price by rememberSaveable(filters.maxPrice) {
        mutableStateOf<Float?>(filters.maxPrice?.toFloat())
    }
    var sort by rememberSaveable(filters.sort) {
        mutableStateOf<SearchSortEnum>(filters.sort)
    }

    val options = listOf(
        Triple(1, "Reduceri", R.drawable.ic_percent_badge_outline),
        Triple(2, "Last Minute", R.drawable.ic_bolt_outline)
    )

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
                leftText = "Optiuni",
                rightText = ""
            )

            LazyRow(
                modifier = Modifier.padding(top = SpacingM),
                contentPadding = PaddingValues(horizontal = BasePadding)
            ) {
                itemsIndexed(options) { index, item ->
                    MainButtonOutlined(
                        icon = painterResource(item.third),
                        onClick = {},
                        title = item.second
                    )

                    if(index <= options.size) {
                        Spacer(Modifier.width(SpacingS))
                    }
                }
            }

            Spacer(Modifier.height(SpacingXXL))

            SearchSheetInfo(
                leftText = stringResource(R.string.maximumPrice),
                rightText = "${price ?: 1500f} RON"
            )

            CustomSlider(
                modifier = Modifier.padding(SpacingXL),
                value = price?.toFloat() ?: 1500f,
                onValueChange = { price = it },
                valueRange = 0f..1500f
            )

            Spacer(Modifier.height(BasePadding))

            SearchSheetInfo(
                leftText = stringResource(R.string.sortBy),
                rightText = ""
            )

            SearchSortEnum.entries.forEach { option ->
                InputRadio(
                    selected = sort == option,
                    onSelect = { sort = option },
                    headLine = stringResource(option.labelRes),
                    paddingHorizontal = BasePadding
                )
            }
        }

        Column {
            HorizontalDivider(color = Divider, thickness = 0.55.dp)

            SearchSheetActions(
                onClear = onClear,
                onConfirm = { onFilter(price?.toFloat(), sort) },
                isConfirmEnabled = filters.maxPrice != price || filters.sort != sort
            )
        }
    }
}