package com.example.scrollbooker.ui.search.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun SearchCardBusinessInfo(
    modifier: Modifier = Modifier,
    fullName: String,
    ratingsAverage: Float,
    ratingsCount: Int,
    profession: String,
    address: String,
    distance: String?
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = fullName,
                style = titleMedium.copy(fontWeight = FontWeight.SemiBold),
                fontSize = 18.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SpacingXS)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_star_solid),
                    contentDescription = null,
                    tint = Primary
                )

                Text(
                    text = "$ratingsAverage",
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "($ratingsCount)",
                    color = Color.Gray
                )
            }
        }

        Text(
            text = profession,
            style = bodyLarge,
            color = Color.Gray,
        )

        Spacer(Modifier.height(SpacingS))

        Row(verticalAlignment = Alignment.CenterVertically) {
            distance?.let { d ->
                Text(
                    text = d,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = "\u2022",
                color = Color.Gray
            )

            Text(
                modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                text = address,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}