package com.example.scrollbooker.ui.appointments.sheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.list.ItemList
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Error

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancelReviewSheet(
    isLoadingDelete: Boolean,
    onClose: () -> Unit,
    onEdit: () -> Unit,
    onDeleteReview: () -> Unit
) {
    Column {
        SheetHeader(
            title = stringResource(R.string.yourReview),
            onClose = onClose
        )

        ItemList(
            headLine = stringResource(R.string.edit),
            displayRightIcon = false,
            leftIcon = painterResource(R.drawable.ic_edit_outline),
            onClick = onEdit,
        )

        ItemList(
            headLine = stringResource(R.string.delete),
            displayRightIcon = false,
            onClick = onDeleteReview,
            leftIcon = painterResource(R.drawable.ic_delete_outline),
            color = Error,
            loadingColor = Error,
            isLoading = isLoadingDelete,
        )

        Spacer(Modifier.height(BasePadding))
    }
}