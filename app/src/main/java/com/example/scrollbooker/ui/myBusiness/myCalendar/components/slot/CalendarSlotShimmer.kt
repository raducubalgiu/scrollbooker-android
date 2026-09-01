package com.example.scrollbooker.ui.myBusiness.myCalendar.components.slot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.shimmer.shimmerEffect
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun CalendarSlotShimmer(
    height: Dp,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.width(56.dp)) {
            Spacer(
                modifier = Modifier
                    .padding(start = 8.dp, top = 2.dp)
                    .height(14.dp)
                    .width(40.dp)
                    .clip(shape = ShapeDefaults.Medium)
                    .shimmerEffect()
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(shape = ShapeDefaults.Medium)
                .background(SurfaceBG)
                .padding(8.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .height(14.dp)
                    .width(70.dp)
                    .clip(shape = ShapeDefaults.Medium)
                    .shimmerEffect()
            )

            Spacer(Modifier.height(10.dp))

            Spacer(
                modifier = Modifier
                    .height(12.dp)
                    .width(120.dp)
                    .clip(shape = ShapeDefaults.Medium)
                    .shimmerEffect()
            )
        }
    }
}
