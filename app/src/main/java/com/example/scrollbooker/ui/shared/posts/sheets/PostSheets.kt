package com.example.scrollbooker.ui.shared.posts.sheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.ui.shared.posts.sheets.calendar.PostCalendarSheet
import com.example.scrollbooker.ui.shared.posts.sheets.comments.CommentsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.reviews.ReviewsListSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostSheets(
    sheetState: SheetState,
    sheetContent: PostSheetsContent,
    onClose: () -> Unit
) {
    Sheet(
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = 50.dp),
        onClose = onClose,
        sheetState = sheetState,
    ) {
        Column(modifier = Modifier.fillMaxHeight(1f)) {
            when (val content = sheetContent) {
                is PostSheetsContent.ReviewsSheet -> {
                    ReviewsListSheet(
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
                is PostSheetsContent.None -> Unit
            }
        }
    }
}