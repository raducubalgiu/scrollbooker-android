package com.example.scrollbooker.core.nav.host

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.inputs.SearchBar
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AppDrawer() {
    var search by remember { mutableStateOf("") }

    BoxWithConstraints {
        val drawerWidth = maxWidth * 0.7f

        ModalDrawerSheet(
            modifier = Modifier.width(drawerWidth),
            drawerContainerColor = Color(0xFF121212)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(BasePadding)
            ) {
                SearchBar(
                    value = search,
                    containerColor = Color(0xFF1C1C1C),
                    contentColor = Color(0xFF3A3A3A),
                    onValueChange = { search = it },
                    placeholder = "Ce cauti?",
                    onSearch = {}
                )

                HorizontalDivider(modifier = Modifier
                    .padding(vertical = BasePadding),
                    color = Color(0xFF3A3A3A)
                )

                Text(
                    style = titleMedium,
                    color = Color(0xFF3A3A3A),
                    fontWeight = FontWeight.Normal,
                    text = "Filtrează conținutul video"
                )

                Spacer(Modifier.height(BasePadding))

                ListItem(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color(0xFF121212)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(),
                            onClick = {},
                        ),
                    headlineContent = {
                        Text(
                            style = bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = "Frizerie",
                        )
                    },
                    trailingContent = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = null
                        )
                    },
                    colors = ListItemColors(
                        containerColor = Color(0xFF121212),
                        headlineColor = Color(0xFFAAAAAA),
                        leadingIconColor = Background,
                        overlineColor = Color(0xFFAAAAAA),
                        supportingTextColor = Color.Transparent,
                        trailingIconColor = Divider,
                        disabledHeadlineColor = Color(0xFFAAAAAA),
                        disabledLeadingIconColor = Color(0xFFAAAAAA),
                        disabledTrailingIconColor = Color(0xFFAAAAAA)
                    ),
                )
                ListItem(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color(0xFF121212)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(),
                            onClick = {},
                        ),
                    headlineContent = {
                        Text(
                            style = bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = "Frizerie",
                        )
                    },
                    trailingContent = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = null
                        )
                    },
                    colors = ListItemColors(
                        containerColor = Color(0xFF121212),
                        headlineColor = Color(0xFFAAAAAA),
                        leadingIconColor = Background,
                        overlineColor = Color(0xFFAAAAAA),
                        supportingTextColor = Color.Transparent,
                        trailingIconColor = Divider,
                        disabledHeadlineColor = Color(0xFFAAAAAA),
                        disabledLeadingIconColor = Color(0xFFAAAAAA),
                        disabledTrailingIconColor = Color(0xFFAAAAAA)
                    ),
                )

                ListItem(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color(0xFF121212)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(),
                            onClick = {},
                        ),
                    headlineContent = {
                        Text(
                            style = bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = "Frizerie",
                        )
                    },
                    trailingContent = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = null
                        )
                    },
                    colors = ListItemColors(
                        containerColor = Color(0xFF121212),
                        headlineColor = Color(0xFFAAAAAA),
                        leadingIconColor = Background,
                        overlineColor = Color(0xFFAAAAAA),
                        supportingTextColor = Color.Transparent,
                        trailingIconColor = Divider,
                        disabledHeadlineColor = Color(0xFFAAAAAA),
                        disabledLeadingIconColor = Color(0xFFAAAAAA),
                        disabledTrailingIconColor = Color(0xFFAAAAAA)
                    ),
                )

                ListItem(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color(0xFF121212)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(),
                            onClick = {},
                        ),
                    headlineContent = {
                        Text(
                            style = bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = "Frizerie",
                        )
                    },
                    trailingContent = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = null
                        )
                    },
                    colors = ListItemColors(
                        containerColor = Color(0xFF121212),
                        headlineColor = Color(0xFFAAAAAA),
                        leadingIconColor = Background,
                        overlineColor = Color(0xFFAAAAAA),
                        supportingTextColor = Color.Transparent,
                        trailingIconColor = Divider,
                        disabledHeadlineColor = Color(0xFFAAAAAA),
                        disabledLeadingIconColor = Color(0xFFAAAAAA),
                        disabledTrailingIconColor = Color(0xFFAAAAAA)
                    ),
                )
            }
        }
    }
}