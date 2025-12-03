package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun SearchBusinessDomainLabel(
    onClick: () -> Unit,
    isSelected: Boolean,
    name: String
) {
    Surface(
        modifier = Modifier
            .padding(end = 8.dp)
            .clickable { onClick() },
        shape = ShapeDefaults.ExtraLarge,
        color = if(isSelected) Primary else Background,
        shadowElevation = 6.dp,
        tonalElevation = 0.dp
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 10.dp),
            text = name,
            color = if(isSelected) OnPrimary else OnBackground,
            fontWeight = if(isSelected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}