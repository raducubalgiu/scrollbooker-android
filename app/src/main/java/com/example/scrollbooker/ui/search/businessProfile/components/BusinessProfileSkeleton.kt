package com.example.scrollbooker.ui.search.businessProfile.components
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.divider.VerticalDivider
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.profile.components.userInfo.components.CounterItem
import com.example.scrollbooker.ui.profile.components.userInfo.components.ProfileActionButton
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun BusinessProfileSkeleton() {
    val brush = rememberShimmerBrush()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Box {
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(brush)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(8.dp)
                    .size(36.dp)
                    .zIndex(3f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {},
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = SurfaceBG
                    ),
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Background)
                ) {}

                IconButton(
                    onClick = {},
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = SurfaceBG
                    ),
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Background)
                ) {}
            }
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = SpacingXL),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(
                    modifier = Modifier
                        .size(75.dp)
                        .clip(CircleShape)
                        .background(brush)
                )

                Spacer(Modifier.width(BasePadding))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            repeat(3) { index ->
                                CounterItem(
                                    counter = null,
                                    label = stringResource(R.string.reviews),
                                    onNavigate = {}
                                )

                                if (index < 3) {
                                    VerticalDivider()
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(BasePadding))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ProfileActionButton(
                            modifier = Modifier.weight(5f),
                            height = 40.dp,
                            containerColor = SurfaceBG,
                            contentColor = OnBackground,
                            shape = ShapeDefaults.ExtraLarge,
                            onClick = { }
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .height(12.5.dp)
                                    .fillMaxWidth(fraction = 0.4f)
                                    .background(brush)
                            )
                        }

                        Spacer(Modifier.width(SpacingS))

                        ProfileActionButton(
                            modifier = Modifier.weight(5f),
                            height = 40.dp,
                            shape = ShapeDefaults.ExtraLarge,
                            containerColor = SurfaceBG,
                            contentColor = OnBackground,
                            onClick = { }
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .height(12.5.dp)
                                    .fillMaxWidth(fraction = 0.4f)
                                    .background(brush)
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.4f)
                    .height(20.dp)
                    .background(brush)
            )

            Spacer(Modifier.height(SpacingM))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.7f)
                    .height(17.5.dp)
                    .background(brush)
            )

            Spacer(Modifier.height(SpacingM))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.3f)
                    .height(17.5.dp)
                    .background(brush)
            )

            Spacer(Modifier.height(SpacingM))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.3f)
                    .height(17.5.dp)
                    .background(brush)
            )

            Spacer(Modifier.height(SpacingXXL))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.3f)
                    .height(20.dp)
                    .background(brush)
            )

            Spacer(Modifier.height(SpacingXXL))
        }
    }
}