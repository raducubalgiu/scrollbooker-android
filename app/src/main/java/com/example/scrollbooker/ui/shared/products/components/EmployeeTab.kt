package com.example.scrollbooker.ui.shared.products.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun EmployeeTab(
    avatar: String?,
    fullName: String,
    ratingsAverage: Float,
    onSelectedEmployee: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(ShapeDefaults.Medium)
            .padding(vertical = SpacingS)
            .clickable(
                onClick = onSelectedEmployee,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AvatarWithRating(
                modifier = Modifier.size(70.dp),
                url = "$avatar",
                rating = ratingsAverage,
                onClick = {}
            )
            Spacer(Modifier.height(SpacingM))
            Text(
                modifier = Modifier.padding(horizontal = BasePadding),
                textAlign = TextAlign.Center,
                text = fullName,
                fontWeight = FontWeight.SemiBold,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}