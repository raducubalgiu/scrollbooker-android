package com.example.scrollbooker.components.core.headers
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    title: String = "",
    withBackground: Boolean = true,
    customTitle: (@Composable () -> Unit)? = null,
    actions:  @Composable (RowScope.() -> Unit) = {},
) {
    val containerColor = if(withBackground) Background else Color.Transparent
    val contentColor = if(withBackground) OnBackground else Color.White

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            if(customTitle != null) {
                customTitle()
            } else {
                title.isNotEmpty().let {
                    Text(
                        style = titleMedium,
                        color = contentColor,
                        fontWeight = FontWeight.Bold,
                        text = title
                    )
                }
            }
        },
        navigationIcon = {
            onBack?.let {
                CustomIconButton(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    onClick = onBack,
                    tint = contentColor
                )
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = containerColor,
            scrolledContainerColor = containerColor,
            navigationIconContentColor = contentColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor
        )
    )
}