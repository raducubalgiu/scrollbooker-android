package com.example.scrollbooker.components.customized.post.sheets.linkedProducts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.UserProfileParam

@Composable
fun LinkedProductsSheet(
    modifier: Modifier = Modifier,
    post: Post,
    onClose: () -> Unit,
    onNavigateToBooking: (product: Product) -> Unit,
    onNavigateToBookingFromAppointment: (Appointment) -> Unit,
    onNavigateToBookingFromProfile: (businessId: Int, userId: Int, businessOwnerId: Int) -> Unit,
    onNavigateToUserProfile: (UserProfileParam) -> Unit
) {
    val viewModel: LinkedProductsViewModel = hiltViewModel()

    LaunchedEffect(post.id, post.user.id, post.isVideoReview) {
        viewModel.setPost(post.id, post.user.id, post.isVideoReview)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.85f)
    ) {
        SheetHeader(
            title = stringResource(if (post.isVideoReview) R.string.videoReviewDetails
                else R.string.recommendedServices),
            onClose = onClose
        )

        if (post.isVideoReview) {
            VideoReviewSection(
                viewModel = viewModel,
                post = post,
                onNavigateToUserProfile = onNavigateToUserProfile,
                onNavigateToBookingFromAppointment = onNavigateToBookingFromAppointment,
                onNavigateToBookingFromProfile = onNavigateToBookingFromProfile
            )
        } else {
            LinkedProductsSection(
                viewModel = viewModel,
                onNavigateToBooking = onNavigateToBooking
            )
        }
    }
}
