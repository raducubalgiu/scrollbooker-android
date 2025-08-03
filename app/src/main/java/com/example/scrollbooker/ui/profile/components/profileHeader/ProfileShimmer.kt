package com.example.scrollbooker.ui.profile.components.profileHeader

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.divider.VerticalDivider
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.profile.components.profileHeader.components.CounterItem
import com.example.scrollbooker.ui.profile.components.profileHeader.components.ProfileActionButton
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun ProfileShimmer() {
    val brush = rememberShimmerBrush()

    Header(title = "")

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(
            vertical = SpacingXXL,
            horizontal = 70.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(3) { index ->
            CounterItem(
                counter = null,
                label = stringResource(R.string.reviews),
                onNavigate = {}
            )
            if(index < 5) {
                VerticalDivider()
            }
        }
    }
    Column(modifier = Modifier
        .padding(horizontal = SpacingXL)
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Avatar(
                    url = "",
                    size = 90.dp
                )

                Box(modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFCCCCCC))
                    .border(3.dp, Color.White, CircleShape)
                )
            }
            Spacer(Modifier.width(BasePadding))

            Column {
                Spacer(modifier = Modifier
                    .height(12.5.dp)
                    .fillMaxWidth(fraction = 0.3f)
                    .background(brush)
                )

                Spacer(Modifier.height(SpacingS))

                Spacer(modifier = Modifier
                    .height(12.5.dp)
                    .fillMaxWidth(fraction = 0.4f)
                    .background(brush)
                )

                Spacer(Modifier.height(SpacingS))

                Spacer(modifier = Modifier
                    .height(12.5.dp)
                    .fillMaxWidth(fraction = 0.6f)
                    .background(brush)
                )
            }
        }

        Spacer(Modifier.height(SpacingXL))

        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileActionButton(
                modifier = Modifier.weight(5f),
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
            Spacer(Modifier.width(SpacingS))
            ProfileActionButton(
                modifier = Modifier.weight(5f),
                onClick = {}
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
                modifier = Modifier.weight(1.5f),
                onClick = {}
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }
    }
}