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
    enablePaddingH: Boolean = true,
    header: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            if(header == null) {
                Header(
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
        Box(modifier = Modifier.padding(innerPadding).then(modifier)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if(enablePaddingH) BasePadding else 0.dp)
            ) { content() }
        }
    }
}