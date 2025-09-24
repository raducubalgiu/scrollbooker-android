package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun SearchSheetHeader(onMeasured: (Dp) -> Unit) {
    val density = LocalDensity.current

    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .onGloballyPositioned {
            val h = with(density) { it.size.height.toDp() }
            onMeasured(h)
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

            Text(
                modifier = Modifier.padding(top = BasePadding),
                text = "200 de rezultate",
                style = titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}