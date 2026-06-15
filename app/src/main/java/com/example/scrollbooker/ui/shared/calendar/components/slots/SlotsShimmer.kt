package com.example.scrollbooker.ui.shared.calendar.components.slots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.shimmer.shimmerEffect
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun SlotsShimmer() {
    Column(Modifier.fillMaxSize().padding(horizontal = BasePadding)) {
        repeat(5) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(shape = ShapeDefaults.Large)
                    .background(SurfaceBG)
                    .padding(BasePadding),
                contentAlignment = Alignment.CenterStart
            ) {
                Spacer(
                    modifier = Modifier
                        .height(15.dp)
                        .width(60.dp)
                        .clip(shape = ShapeDefaults.Medium)
                        .shimmerEffect()
                )
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}