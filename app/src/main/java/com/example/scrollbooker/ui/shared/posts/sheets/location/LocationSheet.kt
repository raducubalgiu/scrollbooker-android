package com.example.scrollbooker.ui.shared.posts.sheets.location
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.sheet.SheetHeader

@Composable
fun LocationSheet(
    onClose: () -> Unit,
) {
    SheetHeader(
        title = stringResource(R.string.location),
        onClose = onClose
    )
}