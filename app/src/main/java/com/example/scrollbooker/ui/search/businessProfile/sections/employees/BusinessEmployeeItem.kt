package com.example.scrollbooker.ui.search.businessProfile.sections.employees

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun BusinessEmployeeItem(
    fullName: String,
    profession: String,
    avatar: String,
    ratingsAverage: Float,
    onNavigateToUserProfile: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onNavigateToUserProfile
        ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AvatarWithRating(
            url = avatar,
            size = 85.dp,
            rating = ratingsAverage,
            onClick = onNavigateToUserProfile
        )

        Spacer(Modifier.height(SpacingXL))

        Text(
            text = fullName,
            fontSize = 18.sp,
            style = titleMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(SpacingXS))

        Text(
            text = profession,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}