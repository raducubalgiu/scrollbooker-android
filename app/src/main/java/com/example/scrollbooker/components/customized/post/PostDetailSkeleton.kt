package com.example.scrollbooker.components.customized.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.BackgroundDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailSkeleton(
    onBack: () -> Unit,
    title: String = "",
) {
    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            Header(
                onBack = onBack,
                title = title,
                icon = Icons.Default.Close,
                iconSize = 30.dp,
                containerColor = Color.Transparent,
                contentColor = Color.White
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark)
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            Box(modifier = Modifier.weight(1f)) {
                LoadingScreen(color = Color.White)
            }

            MainButton(
                modifier = Modifier.padding(
                    vertical = SpacingS,
                    horizontal = BasePadding
                ),
                contentPadding = PaddingValues(12.dp),
                enabled = false,
                onClick = {},
                title = stringResource(R.string.bookNow),
            )
        }
    }
}
