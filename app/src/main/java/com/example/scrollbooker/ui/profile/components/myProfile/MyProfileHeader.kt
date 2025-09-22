package com.example.scrollbooker.ui.profile.components.myProfile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.IconSizeXL
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.titleMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileHeader(
    modifier: Modifier = Modifier,
    username: String,
    isBusinessOrEmployee: Boolean,
    onOpenBottomSheet: () -> Unit,
    onNavigateToCreatePost: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    style = titleMedium,
                    color = OnBackground,
                    fontWeight = FontWeight.Bold,
                    text = "@${username}"
                )
            }
        },
        colors = TopAppBarColors(
            containerColor = Background,
            scrolledContainerColor = Background,
            navigationIconContentColor = OnBackground,
            titleContentColor = OnBackground,
            actionIconContentColor = OnBackground
        ),
        navigationIcon = {
            Box(modifier = Modifier
                .size(if(isBusinessOrEmployee) 100.dp else 50.dp)
            )
        },
        actions = {
            Row {
                if(isBusinessOrEmployee) {
                    Box(modifier = Modifier
                        .size(50.dp)
                        .clickable(
                            onClick = onNavigateToCreatePost,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddBox,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(IconSizeXL)
                        )
                    }
                }

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
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        tint = OnBackground,
                        modifier = Modifier.size(IconSizeXL)
                    )
                }
            }
        }
    )
}