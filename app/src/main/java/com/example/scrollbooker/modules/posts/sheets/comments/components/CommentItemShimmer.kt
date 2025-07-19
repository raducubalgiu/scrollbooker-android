package com.example.scrollbooker.modules.posts.sheets.comments.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXS
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS

@Composable
fun CommentItemShimmer() {
    val brush = rememberShimmerBrush()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding),
    ) {
        Spacer(modifier = Modifier
            .size(AvatarSizeXS)
            .clip(CircleShape)
            .background(brush)
        )
        Spacer(Modifier.width(SpacingS))
        Column {
            Spacer(Modifier.height(SpacingXS))
            Spacer(
                modifier = Modifier
                    .height(15.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(fraction = 0.4f)
                    .background(brush)
            )
            Spacer(Modifier.height(SpacingXS))
            Spacer(
                modifier = Modifier
                    .height(15.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(fraction = 0.7f)
                    .background(brush)
            )
            Spacer(Modifier.height(SpacingM))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(
                    modifier = Modifier
                        .height(15.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth(fraction = 0.2f)
                        .background(brush)
                )
                Spacer(
                    modifier = Modifier
                        .height(15.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth(fraction = 0.2f)
                        .background(brush)
                )
            }

            Spacer(Modifier.height(SpacingS))
        }
    }

    Spacer(Modifier.height(BasePadding))
}