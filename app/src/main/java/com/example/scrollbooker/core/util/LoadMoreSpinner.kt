package com.example.scrollbooker.core.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun LoadMoreSpinner(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(BasePadding)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .size(30.dp)
            .then(modifier),

    )
}