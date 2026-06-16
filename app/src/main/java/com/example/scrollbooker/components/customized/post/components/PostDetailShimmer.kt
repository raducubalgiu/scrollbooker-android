package com.example.scrollbooker.components.customized.post.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.BackgroundDark

@Composable
fun PostDetailShimmer(modifier: Modifier = Modifier) {
    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            Header(
                onBack = {},
                title = "",
                icon = Icons.Default.Close,
                iconSize = 30.dp,
                withBackground = false
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark)
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
           PostShimmer()

            MainButton(
                modifier = Modifier.padding(
                    vertical = SpacingS,
                    horizontal = BasePadding
                ),
                contentPadding = PaddingValues(12.dp),
                onClick = {},
                title = stringResource(R.string.bookNow),
            )
        }
    }
}