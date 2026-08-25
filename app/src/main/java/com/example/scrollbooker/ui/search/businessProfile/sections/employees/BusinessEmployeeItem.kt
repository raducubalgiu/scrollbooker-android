package com.example.scrollbooker.ui.search.businessProfile.sections.employees

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileEmployee
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun BusinessEmployeeItem(
    employee: BusinessProfileEmployee,
    onNavigateToEmployeeProfile: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(90.dp)
            .clickable(
                onClick = onNavigateToEmployeeProfile,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AvatarWithRating(
                size = 80.dp,
                url = "${employee.avatar}",
                rating = employee.ratingsAverage,
                onClick = onNavigateToEmployeeProfile
            )
            Spacer(Modifier.height(BasePadding))
            Text(
                modifier = Modifier.padding(horizontal = BasePadding),
                style = bodyMedium,
                textAlign = TextAlign.Center,
                text = employee.fullName,
                fontWeight = FontWeight.SemiBold,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}