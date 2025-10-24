package com.example.scrollbooker.ui.shared.posts.sheets.location
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.shared.location.LocationSection

@Composable
fun LocationSheet(
    onClose: () -> Unit,
) {
    SheetHeader(
        title = stringResource(R.string.location),
        onClose = onClose
    )

    LocationSection(Modifier.padding(horizontal = SpacingXL))
}