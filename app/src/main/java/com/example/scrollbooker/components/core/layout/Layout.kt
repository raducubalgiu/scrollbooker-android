package com.example.scrollbooker.components.core.layout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun Layout(
    modifier: Modifier = Modifier,
    headerTitle: String = "",
    onBack: (() -> Unit)? = null,
    enableBack: Boolean = true,
    enablePaddingH: Boolean = true,
    header: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            if(header == null) {
                Header(
                    enableBack = enableBack,
                    title = headerTitle,
                    onBack = onBack
                )
            } else {
                Box(Modifier.statusBarsPadding()) {
                    header()
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if(enablePaddingH) BasePadding else 0.dp)
            ) { content() }
        }
    }
}