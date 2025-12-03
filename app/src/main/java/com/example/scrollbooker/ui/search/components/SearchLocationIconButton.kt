package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground

@Composable
fun SearchMapActions(
    paddingBottom: Dp,
    onFlyToUserLocation: () -> Unit,
    onSheetExpand: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = SpacingXL)
                    .shadow(
                        elevation = 2.dp,
                        shape = CircleShape,
                        clip = false
                    )
                    .size(52.dp),
                onClick = onFlyToUserLocation,
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

            Spacer(Modifier.height(SpacingS))

            IconButton(
                modifier = Modifier
                    .padding(bottom = paddingBottom)
                    .padding(horizontal = SpacingXL)
                    .shadow(
                        elevation = 2.dp,
                        shape = CircleShape,
                        clip = false
                    )
                    .size(52.dp),
                onClick = onSheetExpand,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Background,
                    contentColor = OnBackground
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_list_bullet_outline),
                    contentDescription = null
                )
            }
        }
    }
}