package com.example.scrollbooker.components.customized

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS

@Composable
fun ProductCardShimmer() {
    val brush = rememberShimmerBrush()

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(BasePadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.4f)
                            .height(SpacingM)
                            .clip(shape = ShapeDefaults.ExtraSmall)
                            .background(brush)
                    )

                    Spacer(Modifier.height(SpacingS))

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.3f)
                            .height(SpacingM)
                            .clip(shape = ShapeDefaults.ExtraSmall)
                            .background(brush)
                    )

                    Spacer(Modifier.height(BasePadding))

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.6f)
                            .height(BasePadding)
                            .clip(shape = ShapeDefaults.ExtraSmall)
                            .background(brush)
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.3f)
                        .height(40.dp)
                        .clip(shape = ShapeDefaults.Medium)
                        .background(brush)
                )
            }
        }
    }
}