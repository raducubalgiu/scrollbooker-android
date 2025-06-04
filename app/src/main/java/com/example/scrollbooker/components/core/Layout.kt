package com.example.scrollbooker.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Background

@Composable
fun Layout(
    modifier: Modifier = Modifier,
    headerTitle: String = "",
    onBack: (() -> Unit)? = null,
    enableBack: Boolean = true,
    header: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Background)
        .then(modifier)
    ){
        if(header != null) {
            Header(
                enableBack = enableBack,
                title = headerTitle,
                onBack = onBack
            )
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(BasePadding)
        ) { content() }
    }
}