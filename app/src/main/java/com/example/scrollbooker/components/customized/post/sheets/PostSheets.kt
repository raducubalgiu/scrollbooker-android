package com.example.scrollbooker.components.customized.post.sheets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.customized.post.sheets.PendingPostAction.*
import com.example.scrollbooker.components.customized.post.sheets.comments.CommentsSheet
import com.example.scrollbooker.components.customized.post.sheets.linkedProducts.LinkedProductsSheet
import com.example.scrollbooker.components.customized.post.sheets.more.MoreSheet
import com.example.scrollbooker.components.customized.post.sheets.reviews.ReviewsSheet
import com.example.scrollbooker.components.customized.post.sheets.statistics.PostStatisticsSheet
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import kotlinx.coroutines.launch

sealed interface PendingPostAction {
    data class Book(val product: Product) : PendingPostAction
    data class Edit(val postId: Int) : PendingPostAction
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostSheets(
    sheetState: SheetState,
    sheetContent: PostSheetsContent,
    onNavigateToBooking: (product: Product) -> Unit,
    onNavigateToEditPost: (Int) -> Unit,
    onOpenStatisticsSheet: (postId: Int) -> Unit,
    onClose: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var pendingAction by remember { mutableStateOf<PendingPostAction?>(null) }

    LaunchedEffect(sheetState.isVisible) {
        if (!sheetState.isVisible && pendingAction != null) {
            when (val action = pendingAction) {
                is Book -> onNavigateToBooking(action.product)
                is Edit -> onNavigateToEditPost(action.postId)
                else -> Unit
            }
            pendingAction = null
            onClose()
        }
    }

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
                    onClose = onClose
                )
            }
            is PostSheetsContent.LinkedProductsSheet -> {
                LinkedProductsSheet(
                    onClose = onClose,
                    postId = content.postId,
                    onNavigateToBooking = { product ->
                        pendingAction = Book(product)
                        scope.launch { sheetState.hide() }
                    }
                )
            }
            is PostSheetsContent.MoreSheet -> {
                MoreSheet(
                    postId = content.postId,
                    onClose = onClose,
                    onNavigateToEditPost = { postId ->
                        pendingAction = Edit(postId)
                        scope.launch { sheetState.hide() }
                    },
                    onOpenStatistics = { onOpenStatisticsSheet(it) },
                )
            }
            is PostSheetsContent.StatisticsSheet -> {
                PostStatisticsSheet(
                    postId = content.postId,
                    onClose = onClose
                )
            }
            is PostSheetsContent.None -> Unit
        }
    }
}