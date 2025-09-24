package com.example.scrollbooker.ui.search.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun SearchSheetHeader(collapsedHeight: Dp) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(collapsedHeight)
        .background(Color.Red),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .padding(top = BasePadding)
                .width(60.dp)
                .height(4.dp)
                .clip(shape = ShapeDefaults.ExtraLarge)
                .background(Divider)
        )
    }
}