package com.example.scrollbooker.components.customized.post.sheets.more

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.list.ItemList
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.ui.theme.Error

@Composable
fun MoreSheet(
    postId: Int,
    onClose: () -> Unit,
    onOpenStatistics: (Int) -> Unit,
    onNavigateToEditPost: (Int) -> Unit,
    onOpenDeleteConfirm: (Int) -> Unit
) {
    val viewModel: MoreViewModel = hiltViewModel()

    LaunchedEffect(postId) {
        viewModel.setPostId(postId)
    }

    Column {
        SheetHeader(
            title = stringResource(R.string.myPost),
            onClose = onClose
        )

        ItemList(
            headLine = stringResource(R.string.statistics),
            leftIcon = painterResource(R.drawable.ic_clipboard_outline),
            displayRightIcon = false,
            onClick = { onOpenStatistics(postId) }
        )

        ItemList(
            headLine = stringResource(id = R.string.edit),
            leftIcon = painterResource(R.drawable.ic_edit_outline),
            displayRightIcon = false,
            onClick = { onNavigateToEditPost(postId) }
        )

        ItemList(
            headLine = stringResource(id = R.string.delete),
            leftIcon = painterResource(R.drawable.ic_delete_outline),
            displayRightIcon = false,
            color = Error,
            onClick = { onOpenDeleteConfirm(postId) }
        )
    }
}
