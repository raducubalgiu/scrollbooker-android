package com.example.scrollbooker.ui.search.components.markers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Background

enum class BusinessCategory { BEAUTY, AUTO, MEDICAL }

object MarkerPalette {
    val Beauty = Color(0xFFFF6F00)
    val Auto = Color(0xFF3A86FF)
    val Medical = Color(0xFF36CFC9)

    fun colorFor(category: BusinessCategory) = when(category) {
        BusinessCategory.BEAUTY -> Beauty
        BusinessCategory.AUTO -> Auto
        BusinessCategory.MEDICAL -> Medical
    }
}

@Composable
fun SearchMarkerDot(
    category: BusinessCategory,
    size: Dp = 18.dp,
    borderWidth: Dp = 3.dp
) {
    val fill = MarkerPalette.colorFor(category)

    Box(modifier = Modifier
        .size(size)
        .background(fill, CircleShape)
        .border(borderWidth, Background, CircleShape)
        .shadow(
            elevation = 4.dp,
            shape = CircleShape,
            clip = false
        )
    )
}