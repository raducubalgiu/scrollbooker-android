package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun SearchBusinessDomainLabel(
    onClick: () -> Unit,
    isSelected: Boolean,
    name: String,
    shadowElevation: Dp,
    activeContainerColor: Color = Primary,
    activeContentColor: Color = OnPrimary,
    inactiveContainerColor: Color,
    inactiveContentColor: Color,
    paddingValues: PaddingValues,
    shape: Shape
) {
    Surface(
        modifier = Modifier
            .padding(end = 8.dp)
            .clickable { onClick() },
        shape = shape,
        color = if(isSelected) activeContainerColor else inactiveContainerColor,
        shadowElevation = shadowElevation,
        tonalElevation = 0.dp
    ) {
        Text(
            modifier = Modifier.padding(paddingValues),
            text = name,
            color = if(isSelected) activeContentColor else inactiveContentColor,
            fontWeight = if(isSelected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}