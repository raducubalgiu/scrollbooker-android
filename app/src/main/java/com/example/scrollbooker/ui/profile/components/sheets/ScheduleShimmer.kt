package com.example.scrollbooker.ui.profile.components.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM

@Composable
fun ScheduleShimmer() {
    val brush = rememberShimmerBrush()

    repeat(7) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(
                    horizontal = BasePadding,
                    vertical = SpacingM
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier
                .fillMaxWidth(fraction = 0.4f)
                .height(BasePadding)
                .background(brush)
            )
            Spacer(Modifier
                .fillMaxWidth(fraction = 0.2f)
                .height(BasePadding)
                .background(brush)
            )
        }
    }
}