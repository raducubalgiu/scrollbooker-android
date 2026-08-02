package com.example.scrollbooker.ui.camera.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun CreatePostBottomBar(
    onCreate: () -> Unit,
    isLoading: Boolean,
    isDisabled: Boolean
) {
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Column {
        HorizontalDivider(color = Divider, thickness = 0.55.dp)
        MainButton(
            modifier = Modifier
                .padding(horizontal = BasePadding)
                .padding(bottom = bottomInset),
            title = stringResource(R.string.postNow),
            onClick = onCreate,
            isLoading = isLoading,
            enabled = !isDisabled
        )
    }
}
