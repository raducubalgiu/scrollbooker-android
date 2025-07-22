package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun SearchBusinessTypeItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isSystemInDarkMode = isSystemInDarkTheme()
    val background = if(isSystemInDarkMode) SurfaceBG else Background
    val color = if(isSystemInDarkMode) OnSurfaceBG else OnSurfaceBG

    Box(
        modifier = Modifier
            .padding(top = SpacingM)
            .shadow(
                elevation = 2.dp,
                shape = CircleShape,
                clip = false
            )
            .clip(shape = ShapeDefaults.ExtraLarge)
            .background(if(isSelected) Primary else background)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier.padding(
                vertical = SpacingM,
                horizontal = BasePadding
            )
        ) {
            Text(
                text = title,
                style = titleMedium,
                fontWeight = if(isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if(isSelected) OnPrimary else color
            )
        }
    }
}