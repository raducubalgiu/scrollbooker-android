package com.example.scrollbooker.ui.search.businessProfile.tabs
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.core.divider.VerticalDivider
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.profile.components.userInformation.components.CounterItem
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun BusinessPhotosTab(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = BasePadding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = BasePadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarWithRating(
                url = "https://media.scrollbooker.ro/logo.jpg",
                size = 100.dp,
                rating = "4.5"
            )

            Column(Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CounterItem(
                        counter = 120,
                        label = "Recenzii",
                        onNavigate = {}
                    )

                    VerticalDivider()

                    CounterItem(
                        counter = 500,
                        label = "Urmaritori",
                        onNavigate = {}
                    )
                }

                Spacer(Modifier.height(BasePadding))

                MainButtonOutlined(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = BasePadding),
                    title = stringResource(R.string.follow),
                    onClick = {}
                )
            }
        }

        Text(
            text = "House Of Barbers",
            style = titleLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(Modifier.height(SpacingM))

        Text(
            text = "Strada Randunelelor, nr 45, Sector 2",
            color = Color.Gray,
            style = bodyLarge,
            fontSize = 17.sp
        )

        Spacer(Modifier.height(SpacingM))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_location_outline),
                contentDescription = null,
                tint = Color.Gray
            )
            Spacer(Modifier.width(SpacingS))
            Text(
                text = "la 5km de tine",
                color = Color.Gray,
                style = bodyLarge,
                fontSize = 17.sp
            )
        }

        Spacer(Modifier.height(SpacingM))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .width(10.dp)
                    .height(10.dp)
                    .background(Color.Green)
            )

            Spacer(Modifier.width(SpacingS))

            Text(
                text = "Deschis acum",
                color = Color.Gray,
                style = bodyLarge,
                fontSize = 17.sp
            )
        }
    }
}