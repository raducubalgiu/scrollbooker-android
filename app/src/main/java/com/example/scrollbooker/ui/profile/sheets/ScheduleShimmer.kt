package com.example.scrollbooker.ui.profile.sheets
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.shimmer.shimmerEffect
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM

@Composable
fun ScheduleShimmer() {
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
                .shimmerEffect()
            )
            Spacer(Modifier
                .fillMaxWidth(fraction = 0.2f)
                .height(BasePadding)
                .shimmerEffect()
            )
        }
    }
}