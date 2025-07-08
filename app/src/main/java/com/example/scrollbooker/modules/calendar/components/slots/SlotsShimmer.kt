package com.example.scrollbooker.modules.calendar.components.slots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun SlotsShimmer() {
    val brush = rememberShimmerBrush()

    Column(Modifier.fillMaxSize()) {
        repeat(10) {
            Spacer(Modifier.height(BasePadding))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(shape = ShapeDefaults.Medium)
                    .background(brush)
                    .padding(BasePadding)
            )
        }
    }
}