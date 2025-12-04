package com.example.scrollbooker.ui.search.sheets.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun SearchFiltersButton(
    activeFiltersCount: Int,
    onFilter: () -> Unit
) {
    Box(
        modifier = Modifier.padding(SpacingS)
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = if(activeFiltersCount > 0) 2.dp else 1.dp,
                    color = if(activeFiltersCount > 0) OnBackground else Divider,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .clickable { onFilter() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .padding(SpacingM)
                    .size(22.5.dp),
                imageVector = Icons.Outlined.Tune,
                contentDescription = null
            )
        }

        if (activeFiltersCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp)
                    .size(25.dp)
                    .border(width = 2.dp, shape = ShapeDefaults.ExtraLarge, color = Background)
                    .background(
                        color = Primary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = activeFiltersCount.toString(),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}