package com.example.scrollbooker.ui.search.businessProfile.sections.summary

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.formatOpeningHours
import com.example.scrollbooker.entity.user.userProfile.domain.model.OpeningHours
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun BusinessSummaryDetails(
    distance: Float?,
    fullName: String,
    address: String,
    openingHours: OpeningHours
) {
    Text(
        text = fullName,
        style = titleLarge,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )

    Spacer(Modifier.height(SpacingXS))

    Row(verticalAlignment = Alignment.CenterVertically) {
        distance?.let {
            Text(
                text = "${it}km",
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = "\u2022",
                color = Color.Gray
            )
        }

        Text(
            modifier = Modifier.fillMaxWidth(fraction = 0.8f),
            text = address,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    Spacer(Modifier.height(SpacingM))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if(openingHours.openNow) stringResource(R.string.open)
            else stringResource(R.string.closed),
            color = OnBackground,
            style = titleMedium,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            modifier = Modifier.padding(horizontal = 5.dp),
            text = "\u2022",
            color = Color.Gray
        )

        Text(
            text = formatOpeningHours(openingHours).toString(),
            color = Color.Gray,
            style = bodyLarge,
        )
    }
}