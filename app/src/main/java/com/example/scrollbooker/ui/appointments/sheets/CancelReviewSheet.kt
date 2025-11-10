package com.example.scrollbooker.ui.appointments.sheets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.list.ItemList
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.appointments.AppointmentsViewModel
import com.example.scrollbooker.ui.theme.Error
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancelReviewSheet(
    viewModel: AppointmentsViewModel,
    sheetState: SheetState,
    isLoadingDelete: Boolean,
    onClose: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val deleteState by viewModel.deleteReviewState.collectAsState()

    LaunchedEffect(deleteState) {
        when(deleteState) {
            is FeatureState.Success -> {
                viewModel.consumeCreateReviewState()
                onClose()
            }
            is FeatureState.Error -> {
                viewModel.consumeCreateReviewState()
            }
            else -> Unit
        }
    }

    Sheet(
        sheetState = sheetState,
        onClose = onClose
    ) {
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
            onClick = onDelete,
            leftIcon = painterResource(R.drawable.ic_delete_outline),
            color = Error,
            loadingColor = Error,
            isLoading = isLoadingDelete,
        )

        Spacer(Modifier.height(BasePadding))
    }
}