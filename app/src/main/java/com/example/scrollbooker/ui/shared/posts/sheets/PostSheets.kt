package com.example.scrollbooker.ui.shared.posts.sheets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.ui.shared.posts.sheets.calendar.PostCalendarSheet
import com.example.scrollbooker.ui.shared.posts.sheets.comments.CommentsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.moreOptions.MoreOptionsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.reviews.ReviewsSheet
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostSheets(
    sheetState: SheetState,
    sheetContent: PostSheetsContent,
    onClose: () -> Unit
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
            is PostSheetsContent.ReviewsSheet -> {
                ReviewsSheet(
                    userId = content.userId,
                    onClose = onClose,
                )
            }
            is PostSheetsContent.CommentsSheet -> {
                CommentsSheet(
                    postId = content.postId,
                    isSheetVisible = sheetState.isVisible,
                    onClose = onClose
                )
            }
            is PostSheetsContent.CalendarSheet -> PostCalendarSheet()
            is PostSheetsContent.LocationSheet -> {}
            is PostSheetsContent.MoreOptionsSheet -> {
                MoreOptionsSheet(
                    onClose = onClose
                )
            }
            is PostSheetsContent.None -> Unit
        }
    }
}