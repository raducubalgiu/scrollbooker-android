package com.example.scrollbooker.screens.profile.myBusiness.myProducts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun MyBusinessCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = Modifier
        .clip(shape = ShapeDefaults.Medium)
        .background(SurfaceBG)
        .clickable(
            onClick = onClick,
            interactionSource = interactionSource,
            indication = null
        )
        .then(modifier)
    ) {
        Column(modifier = Modifier
            .padding(BasePadding)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = OnSurfaceBG
            )

            Spacer(Modifier.height(SpacingXL))

            Text(
                text = title,
                color = OnBackground,
                style = bodyLarge,
                fontWeight = FontWeight.ExtraBold,
                minLines = 1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(BasePadding))

            Text(
                text = description,
                color = Color.Gray,
                style = bodyMedium,
                minLines = 3,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}