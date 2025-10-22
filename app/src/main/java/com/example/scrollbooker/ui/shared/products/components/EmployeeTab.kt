package com.example.scrollbooker.ui.shared.products.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Primary

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun EmployeeTab(
    isSelected: Boolean,
    avatar: String?,
    fullName: String,
    ratingsAverage: Float,
    onSelectedEmployee: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val cardWidth = remember {
        (screenWidth / 3 - BasePadding)
    }

    val animatedBgColor by animateColorAsState(
        targetValue = if(isSelected) Primary.copy(alpha = 0.2f) else Color.Transparent,
        animationSpec = tween(durationMillis = 300),
        label = "Card Selection Background"
    )

    Box(
        modifier = Modifier
            .width(cardWidth)
            .clip(ShapeDefaults.Medium)
            .background(animatedBgColor)
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
                modifier = Modifier.size(90.dp),
                url = "$avatar",
                rating = ratingsAverage,
                onClick = {}
            )
            Spacer(Modifier.height(SpacingM))
            Text(
                text = fullName,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}