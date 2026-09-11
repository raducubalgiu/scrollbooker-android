package com.example.scrollbooker.components.customized.post.sheets

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.navigation.navigators.UserProfileParam

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostSheetsHost(
    state: PostSheetsState,
    onNavigateToBooking: (product: Product) -> Unit,
    onNavigateToBookingFromAppointment: (Appointment) -> Unit,
    onNavigateToBookingFromProfile: (businessId: Int, userId: Int, businessOwnerId: Int) -> Unit,
    onNavigateToEditPost: (Int) -> Unit,
    onNavigateToStatistics: (Int) -> Unit,
    onPostDeleted: (postId: Int) -> Unit,
    onNavigateToUserProfile: (param: UserProfileParam) -> Unit,
) {
    if (state.content != PostSheetsContent.None) {
        key(state.content) {
            PostSheets(
                sheetState = state.sheetState,
                sheetContent = state.content,
                onClose = { state.close() },
                onNavigateToBooking = onNavigateToBooking,
                onNavigateToBookingFromAppointment = onNavigateToBookingFromAppointment,
                onNavigateToBookingFromProfile = onNavigateToBookingFromProfile,
                onNavigateToEditPost = onNavigateToEditPost,
                onNavigateToStatistics = onNavigateToStatistics,
                onOpenDeleteConfirm = { state.swap(PostSheetsContent.DeletePostSheet(it)) },
                onPostDeleted = { postId -> state.close { onPostDeleted(postId) } },
                onNavigateToUserProfile = onNavigateToUserProfile,
            )
        }
    }
}
