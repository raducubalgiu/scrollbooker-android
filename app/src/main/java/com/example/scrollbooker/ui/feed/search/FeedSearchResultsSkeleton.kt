package com.example.scrollbooker.ui.feed.search
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
import com.example.scrollbooker.components.core.shimmer.shimmerEffect
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS

@Composable
fun FeedSearchResultsSkeleton(rows: Int = 8) {
    Column {
        repeat(rows) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BasePadding, vertical = SpacingS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(
                    modifier = Modifier
                        .size(52.5.dp)
                        .clip(CircleShape)
                        .shimmerEffect()
                )

                Spacer(Modifier.width(BasePadding))

                Column {
                    Spacer(
                        modifier = Modifier
                            .height(14.dp)
                            .fillMaxWidth(fraction = 0.45f)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )

                    Spacer(Modifier.height(SpacingS))

                    Spacer(
                        modifier = Modifier
                            .height(12.dp)
                            .fillMaxWidth(fraction = 0.3f)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}
