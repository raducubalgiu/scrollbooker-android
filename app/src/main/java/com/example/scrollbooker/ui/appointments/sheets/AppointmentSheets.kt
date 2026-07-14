package com.example.scrollbooker.ui.appointments.sheets

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentSheets(
    isSaving: Boolean,
    sheetState: SheetState,
    sheetContent: AppointmentSheetsContent,
    onClose: () -> Unit,
    onSaveReview: (RatingReviewUpdate) -> Unit,
    onOpenEditReview: () -> Unit,
    onDeleteReview: () -> Unit,
    onCancelAppointment: (String) -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onDismissRequest = onClose,
        containerColor = Background,
        contentColor = OnBackground,
        dragHandle = {},
        contentWindowInsets = { BottomSheetDefaults.windowInsets },
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false
        ),
    ) {
        when (val content = sheetContent) {
            is AppointmentSheetsContent.ReviewAppointmentSheet -> {
                AddReviewSheet(
                    isSaving = isSaving,
                    reviewUpdate = content.reviewUpdate,
                    user = content.user,
                    onSave = onSaveReview,
                    onClose = onClose,
                )
            }
            is AppointmentSheetsContent.CancelAppointmentSheet -> {
                CancelAppointmentSheet(
                    isSaving = isSaving,
                    onClose = onClose,
                    onCancelAppointment = onCancelAppointment
                )
            }
            is AppointmentSheetsContent.ReviewOptions -> {
                CancelReviewSheet(
                    isLoadingDelete = isSaving,
                    onEdit = onOpenEditReview,
                    onDeleteReview = onDeleteReview,
                    onClose = onClose,
                )
            }
            is AppointmentSheetsContent.None -> Unit
        }
    }
}