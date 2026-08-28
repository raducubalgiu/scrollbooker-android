package com.example.scrollbooker.ui.feed.drawer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.shimmer.shimmerEffect
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL

@Composable
fun FeedDrawerSkeleton() {
    LazyColumn {
        item { FeedDrawerHeader() }

        item {
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )

            Spacer(Modifier.height(SpacingXL))
        }

        items(3) {
            Column(Modifier.fillMaxWidth()) {
                Spacer(
                    Modifier
                        .width(120.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                Spacer(Modifier.height(BasePadding))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    repeat(3) { chipIndex ->
                        Spacer(
                            Modifier
                                .width((70 + chipIndex * 24).dp)
                                .height(36.dp)
                                .clip(CircleShape)
                                .shimmerEffect()
                        )
                    }
                }

                Spacer(Modifier.height(SpacingXL))
            }
        }
    }
}
