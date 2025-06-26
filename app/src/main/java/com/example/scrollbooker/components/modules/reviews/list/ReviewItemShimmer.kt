package com.example.scrollbooker.components.modules.reviews.list

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.AvatarSizeS
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXL

@Composable
fun ReviewItemShimmer() {
    val brush = rememberShimmerBrush()

    Column(Modifier.padding(BasePadding)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(
                modifier = Modifier
                    .size(AvatarSizeS)
                    .clip(CircleShape)
                    .background(brush)
            )
            Spacer(Modifier.width(SpacingM))
            Column {
                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth(fraction = 0.4f)
                        .background(brush)
                )
                Spacer(Modifier.height(SpacingM))
                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth(fraction = 0.1f)
                        .background(brush)
                )
            }
        }

        Spacer(Modifier.height(BasePadding))

        Spacer(
            modifier = Modifier
                .height(15.dp)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth(fraction = 0.4f)
                .background(brush)
        )

        Spacer(Modifier.height(BasePadding))

        Spacer(
            modifier = Modifier
                .height(15.dp)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth(fraction = 0.2f)
                .background(brush)
        )

        Spacer(Modifier.height(BasePadding))

        Spacer(
            modifier = Modifier
                .height(15.dp)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth(fraction = 1f)
                .background(brush)
        )
        Spacer(Modifier.height(SpacingM))
        Spacer(
            modifier = Modifier
                .height(15.dp)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth(fraction = 0.5f)
                .background(brush)
        )

        Spacer(Modifier.height(SpacingXL))

        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(
                modifier = Modifier
                    .height(15.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(fraction = 0.4f)
                    .background(brush)
            )

            Spacer(
                modifier = Modifier
                    .height(15.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(fraction = 0.1f)
                    .background(brush)
            )
        }
    }
}