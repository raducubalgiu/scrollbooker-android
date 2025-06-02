package com.example.scrollbooker.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Background

@Composable
fun Layout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Background)
        .systemBarsPadding()
        .padding(horizontal = BasePadding)
        .then(modifier)
    ){
        content()
    }
}