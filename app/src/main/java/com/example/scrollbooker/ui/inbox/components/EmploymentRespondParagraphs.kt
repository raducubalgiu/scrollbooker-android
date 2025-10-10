package com.example.scrollbooker.ui.inbox.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun EmploymentRespondParagraphs(employerFullName: String) {
    val paragraphs = listOf(
        stringResource(R.string.youWillReceiveAccessToYourOwnCalendarAndAppointments),
        "${stringResource(R.string.youWillBeAbleToEditAndAddServicesWithinYourBusiness)} ${employerFullName}",
        stringResource(R.string.clientsWillBeAbleToSelectYouDirectlyBasedYourAvailability),
        stringResource(R.string.youWillAppearInThePublicBusinessProfile),
        stringResource(R.string.youWillReceiveReviewsFromYourClients),
        stringResource(R.string.youCouldResignAnytime)
    )

    Spacer(Modifier.height(SpacingXXL))

    Text(
        style = titleMedium,
        fontWeight = FontWeight.SemiBold,
        text = stringResource(R.string.hereIsWhatYouShouldNow)
    )
    Spacer(Modifier.height(SpacingS))

    Text(
        style = bodyLarge,
        fontWeight = FontWeight.Normal,
        color = Color.Gray,
        text = "${stringResource(R.string.byAcceptingThisRequest)}:"
    )

    paragraphs.forEach { text ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = BasePadding),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .padding(
                        top = 6.dp,
                        start = BasePadding
                    )
                    .clip(CircleShape)
                    .size(5.dp)
                    .background(OnBackground)
                    .alignBy(FirstBaseline)
            )
            Spacer(Modifier.width(SpacingS))
            Text(
                style = bodyLarge,
                color = Color.Gray,
                text = text
            )
        }
    }

    Spacer(Modifier.height(BasePadding))
}