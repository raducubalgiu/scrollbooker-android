package com.example.scrollbooker.ui.shared.posts.sheets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.scrollbooker.ui.shared.posts.sheets.comments.CommentsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.moreOptions.MoreOptionsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.ProductsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.location.LocationSheet
import com.example.scrollbooker.ui.shared.posts.sheets.reviewDetails.ReviewDetailsScreen
import com.example.scrollbooker.ui.shared.posts.sheets.reviews.ReviewsSheet
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style

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
            is PostSheetsContent.LocationSheet -> {
                LocationSheet(
                    onClose = onClose
                )
            }
            is PostSheetsContent.MoreOptionsSheet -> {
                MoreOptionsSheet(
                    onClose = onClose
                )
            }
            is PostSheetsContent.BookingsSheet -> {
                ProductsSheet(
                    initialPage = 0,
                    userId = content.userId,
                    onClose = onClose
                )
            }
            is PostSheetsContent.ReviewDetailsSheet -> {
                ReviewDetailsScreen(
                    onClose = onClose
                )
            }
            is PostSheetsContent.None -> Unit
        }
    }
}