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
import com.example.scrollbooker.components.customized.post.sheets.deletePost.DeletePostSheet
import com.example.scrollbooker.components.customized.post.sheets.linkedProducts.LinkedProductsSheet
import com.example.scrollbooker.components.customized.post.sheets.more.MoreSheet
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.navigation.navigators.UserProfileParam
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import kotlinx.coroutines.launch

sealed interface PendingPostAction {
    data class Book(val product: Product) : PendingPostAction
    data class BookAppointment(val appointment: Appointment) : PendingPostAction
    data class BookFromProfile(val businessId: Int, val userId: Int, val businessOwnerId: Int) : PendingPostAction
    data class Edit(val postId: Int) : PendingPostAction
    data class Statistics(val postId: Int) : PendingPostAction
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostSheets(
    sheetState: SheetState,
    sheetContent: PostSheetsContent,
    onNavigateToUserProfile: (param: UserProfileParam) -> Unit,
    onNavigateToBooking: (product: Product) -> Unit,
    onNavigateToBookingFromAppointment: (Appointment) -> Unit,
    onNavigateToBookingFromProfile: (businessId: Int, userId: Int, businessOwnerId: Int) -> Unit,
    onNavigateToEditPost: (Int) -> Unit,
    onNavigateToStatistics: (Int) -> Unit,
    onOpenDeleteConfirm: (postId: Int) -> Unit,
    onPostDeleted: (postId: Int) -> Unit,
    onClose: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var pendingAction by remember { mutableStateOf<PendingPostAction?>(null) }

    LaunchedEffect(sheetState.isVisible) {
        if (!sheetState.isVisible && pendingAction != null) {
            when (val action = pendingAction) {
                is Book -> onNavigateToBooking(action.product)
                is BookAppointment -> onNavigateToBookingFromAppointment(action.appointment)
                is BookFromProfile -> onNavigateToBookingFromProfile(action.businessId, action.userId, action.businessOwnerId)
                is Edit -> onNavigateToEditPost(action.postId)
                is Statistics -> onNavigateToStatistics(action.postId)
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
            is PostSheetsContent.CommentsSheet -> {
                CommentsSheet(
                    postId = content.postId,
                    onClose = onClose,
                    onNavigateToUserProfile = onNavigateToUserProfile
                )
            }
            is PostSheetsContent.LinkedProductsSheet -> {
                LinkedProductsSheet(
                    onClose = onClose,
                    post = content.post,
                    onNavigateToBooking = { product ->
                        pendingAction = Book(product)
                        scope.launch { sheetState.hide() }
                    },
                    onNavigateToBookingFromAppointment = { appointment ->
                        pendingAction = BookAppointment(appointment)
                        scope.launch { sheetState.hide() }
                    },
                    onNavigateToBookingFromProfile = { businessId, userId, businessOwnerId ->
                        pendingAction = BookFromProfile(businessId, userId, businessOwnerId)
                        scope.launch { sheetState.hide() }
                    },
                    onNavigateToUserProfile = onNavigateToUserProfile
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
                    onOpenStatistics = { postId ->
                        pendingAction = Statistics(postId)
                        scope.launch { sheetState.hide() }
                    },
                    onOpenDeleteConfirm = { onOpenDeleteConfirm(it) },
                )
            }
            is PostSheetsContent.DeletePostSheet -> {
                DeletePostSheet(
                    postId = content.postId,
                    onClose = onClose,
                    onDeleted = { onPostDeleted(it) }
                )
            }
            is PostSheetsContent.None -> Unit
        }
    }
}