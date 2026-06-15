package com.example.scrollbooker.components.customized.post.sheets.moreOptions
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun MoreOptionsSheet(
    onClose: () -> Unit,
    onDelete: () -> Unit,
    isOwnPost: Boolean
) {
    SheetHeader(onClose = onClose)

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = BasePadding)
    ) {
        ShareButton(
            modifier = Modifier.weight(0.5f),
            icon = Icons.Default.Share,
            text = stringResource(R.string.share)
        )

        Spacer(Modifier.width(BasePadding))

        ShareButton(
            modifier = Modifier.weight(0.5f),
            icon = Icons.Default.Repeat,
            text = stringResource(R.string.repost)
        )
    }

    Spacer(Modifier.height(BasePadding))

    Row(
        modifier = Modifier.padding(horizontal = BasePadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {},
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = SurfaceBG
            )
        ) {
            Icon(
                imageVector = Icons.Default.CopyAll,
                contentDescription = null
            )
        }

        Text(text = stringResource(R.string.copyLink))
    }

    if (isOwnPost) {
        Spacer(Modifier.height(BasePadding))

        Row(
            modifier = Modifier.padding(horizontal = BasePadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onDelete,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = SurfaceBG
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null
                )
            }

            Text(text = stringResource(R.string.deletePost))
        }
    }

    Spacer(Modifier.height(SpacingXL))
}