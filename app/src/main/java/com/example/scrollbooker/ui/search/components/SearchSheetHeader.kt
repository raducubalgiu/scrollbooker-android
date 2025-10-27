package com.example.scrollbooker.ui.search.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.search.sheets.SearchSheetActionEnum
import com.example.scrollbooker.ui.theme.Divider

data class ButtonFilter(
    @StringRes val title: Int,
    val action: SearchSheetActionEnum
)

@Composable
fun SearchSheetHeader(
    onMeasured: (Dp) -> Unit,
    onAction: (SearchSheetActionEnum) -> Unit
) {
    val density = LocalDensity.current

    val latestOnMeasured by rememberUpdatedState(onMeasured)
    val latestOnAction by rememberUpdatedState(onAction)

    val sendAction: (SearchSheetActionEnum) -> Unit = remember {
        {  action -> latestOnAction(action) }
    }

    val buttons = listOf(
        ButtonFilter(title = R.string.services, action = SearchSheetActionEnum.OPEN_SERVICES),
        ButtonFilter(title = R.string.price, action = SearchSheetActionEnum.OPEN_PRICE),
        ButtonFilter(title = R.string.sort, action = SearchSheetActionEnum.OPEN_SORT),
        ButtonFilter(title = R.string.distance, action = SearchSheetActionEnum.OPEN_DISTANCE),
        ButtonFilter(title = R.string.reviews, action = SearchSheetActionEnum.OPEN_RATINGS)
    )

    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .onGloballyPositioned {
            val h = with(density) { it.size.height.toDp() }
            latestOnMeasured(h)
        },
        contentAlignment = Alignment.TopCenter
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = BasePadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(4.dp)
                    .clip(shape = ShapeDefaults.ExtraLarge)
                    .background(Divider)
            )

            Spacer(Modifier.height(BasePadding))

            LazyRow(contentPadding = PaddingValues(horizontal = BasePadding)) {
                itemsIndexed(buttons) { index, button ->
                    MainButtonOutlined(
                        title = stringResource(button.title),
                        onClick = { sendAction(button.action) },
                        showTrailingIcon = true
                    )

                    if(index < buttons.size) {
                        Spacer(Modifier.width(SpacingM))
                    }
                }
            }
        }
    }
}