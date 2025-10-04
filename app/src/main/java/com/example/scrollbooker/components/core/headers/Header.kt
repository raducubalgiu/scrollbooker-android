package com.example.scrollbooker.components.core.headers
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    customTitle: (@Composable () -> Unit)? = null,
    actions:  @Composable (RowScope.() -> Unit) = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            if(customTitle != null) {
                customTitle()
            } else {
                title.isNotEmpty().let {
                    Text(
                        style = titleMedium,
                        color = OnBackground,
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
                    onClick = onBack
                )
            }
        },
        actions = actions,
        colors = TopAppBarColors(
            containerColor = Background,
            scrolledContainerColor = Background,
            navigationIconContentColor = OnBackground,
            titleContentColor = OnBackground,
            actionIconContentColor = OnBackground
        )
    )
}