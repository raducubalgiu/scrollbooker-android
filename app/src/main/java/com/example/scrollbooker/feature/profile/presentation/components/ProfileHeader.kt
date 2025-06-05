package com.example.scrollbooker.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Segment
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.IconSizeXL
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ProfileHeader(
    onOpenBottomSheet: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier
            .size(50.dp)
            .clickable(
                onClick = onOpenBottomSheet,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
            contentAlignment = Alignment.Center
        ) {
//            Icon(
//                imageVector = Icons.Default.ArrowBackIosNew,
//                contentDescription = null
//            )
        }
//        Row(verticalAlignment = Alignment.CenterVertically) {
//
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text(
//                    text = "Deschide maine la 15:00",
//                    style = titleMedium,
//                    fontWeight = FontWeight.SemiBold
//                )
//                Icon(
//                    imageVector = Icons.Outlined.KeyboardArrowDown,
//                    contentDescription = null
//                )
//            }
//        }
        Text(
            style = titleMedium,
            color = OnBackground,
            fontWeight = FontWeight.Bold,
            text = "@house_of_barbers"
        )
        Box(modifier = Modifier
            .size(50.dp)
            .clickable(
                onClick = onOpenBottomSheet,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Segment,
                contentDescription = null,
                tint = OnBackground,
                modifier = Modifier.size(IconSizeXL)
            )
        }
    }
}