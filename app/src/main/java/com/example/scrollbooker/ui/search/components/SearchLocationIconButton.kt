package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground

@Composable
fun SearchLocationIconButton(
    paddingBottom: Dp,
    onClick: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        IconButton(
            modifier = Modifier
                .padding(bottom = paddingBottom)
                .padding(SpacingXL)
                .size(52.dp)
                .align(Alignment.BottomEnd),
            onClick = onClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Background,
                contentColor = OnBackground
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.NearMe,
                contentDescription = null
            )
        }
    }
}