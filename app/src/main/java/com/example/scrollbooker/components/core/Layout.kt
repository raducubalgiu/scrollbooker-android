package com.example.scrollbooker.components.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun Layout(
    modifier: Modifier = Modifier,
    noPadding: Boolean = false,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = if(noPadding) 0.dp else BasePadding)
        .then(modifier)
    ){
        content()
    }
}