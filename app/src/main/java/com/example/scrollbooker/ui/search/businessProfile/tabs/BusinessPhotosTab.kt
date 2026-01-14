package com.example.scrollbooker.ui.search.businessProfile.tabs
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
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
import com.example.scrollbooker.ui.profile.components.userInfo.components.CounterItem
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AvatarWithRating(
                url = "https://media.scrollbooker.ro/logo.jpg",
                rating = 4.5f,
                onClick = {}
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

                        VerticalDivider()

                        CounterItem(
                            counter = 120,
                            label = "Urmareste",
                            onNavigate = {}
                        )
                    }
                }

                Spacer(Modifier.height(BasePadding))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MainButtonOutlined(
                        modifier = Modifier.weight(0.5f),
                        title = stringResource(R.string.call),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary,
                            contentColor = OnPrimary,
                        ),
                        border = BorderStroke(1.dp, Primary),
                        shape = ShapeDefaults.Medium,
                        onClick = {}
                    )

                    Spacer(Modifier.width(SpacingS))

                    MainButtonOutlined(
                        modifier = Modifier.weight(0.5f),
                        title = stringResource(R.string.follow),
                        shape = ShapeDefaults.Medium,
                        onClick = {}
                    )
                }
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
                text = stringResource(R.string.distanceText, 5),
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