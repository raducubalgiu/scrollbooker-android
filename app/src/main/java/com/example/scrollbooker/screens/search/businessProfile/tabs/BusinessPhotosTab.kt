package com.example.scrollbooker.screens.search.businessProfile.tabs

import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun BusinessPhotosTab(modifier: Modifier = Modifier) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(BasePadding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = BasePadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(
                url = "https://media.scrollbooker.ro/logo.jpg",
                size = 75.dp
            )

            Spacer(Modifier.width(SpacingM))

            Column {
                Text(
                    text = "House Of Barbers",
                    style = titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(SpacingS))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "4.5",
                        style = titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp

                    )
                    Spacer(Modifier.width(SpacingS))
                    RatingsStars(rating = 5f, starSize = 22.dp)
                    Spacer(Modifier.width(SpacingS))
                    Text(
                        text = "(1,000)",
                        style = titleMedium
                    )
                }
            }
        }

        Spacer(Modifier.height(SpacingM))

        Text(
            text = "Strada Randunelelor, nr 45, Sector 2",
            color = Color.Gray,
            style = bodyLarge,
            fontSize = 17.sp
        )

        Spacer(Modifier.height(SpacingM))

        Row {
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
            Icon(
                painter = painterResource(R.drawable.ic_clock_outline),
                contentDescription = null,
                tint = Color.Gray
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