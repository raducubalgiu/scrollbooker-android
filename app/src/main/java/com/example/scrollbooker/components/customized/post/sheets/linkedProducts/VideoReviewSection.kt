package com.example.scrollbooker.components.customized.post.sheets.linkedProducts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXXS
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.IconSizeS
import com.example.scrollbooker.core.util.Dimens.SpacingL
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.model.getDurationText
import com.example.scrollbooker.entity.booking.appointment.domain.model.getFiltersSummary
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.model.PostBusinessLocation
import com.example.scrollbooker.entity.social.post.domain.model.PostBusinessOwner
import com.example.scrollbooker.entity.social.post.domain.model.PostEmployee
import com.example.scrollbooker.entity.social.post.domain.model.PostReview
import com.example.scrollbooker.entity.social.post.domain.model.PostUser
import com.example.scrollbooker.navigation.navigators.UserProfileParam
import com.example.scrollbooker.ui.appointments.components.AppointmentProductPrice
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.bodySmall

private data class ReviewedProvider(
    val id: Int,
    val fullName: String,
    val avatar: String?,
    val profession: String,
    val username: String,
    val ratingsAverage: Float,
    val ratingsCount: Int
)

private fun PostEmployee.toReviewedProvider() = ReviewedProvider(id, fullName, avatar, profession, username, ratingsAverage, ratingsCount)
private fun PostBusinessOwner.toReviewedProvider() = ReviewedProvider(id, fullName, avatar, profession, username, ratingsAverage, ratingsCount)

// Owns the reviewAppointmentState fetch (loading/error/success) for a video-review post, mirroring
// how LinkedProductsSection owns productsState for a regular business/employee post.
@Composable
fun ColumnScope.VideoReviewSection(
    viewModel: LinkedProductsViewModel,
    post: Post,
    onNavigateToUserProfile: (UserProfileParam) -> Unit,
    onNavigateToBookingFromAppointment: (Appointment) -> Unit,
    onNavigateToBookingFromProfile: (businessId: Int, userId: Int, businessOwnerId: Int) -> Unit,
) {
    val reviewAppointmentState by viewModel.reviewAppointmentState.collectAsStateWithLifecycle()
    val review = post.review
    val provider = post.employee?.toReviewedProvider() ?: post.businessOwner.toReviewedProvider()

    when (val currentState = reviewAppointmentState) {
        is FeatureState.Loading -> LoadingScreen()
        is FeatureState.Error -> ErrorScreen()
        is FeatureState.Success -> {
            if (review == null) {
                ErrorScreen()
            } else {
                val appointment = currentState.data

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = BasePadding)
                ) {
                    VideoReviewContent(
                        review = review,
                        reviewer = post.user,
                        provider = provider,
                        businessLocation = post.businessLocation,
                        appointment = appointment,
                        onNavigateToUserProfile = onNavigateToUserProfile
                    )
                }

                VideoReviewActions(
                    provider = provider,
                    onBook = { onNavigateToBookingFromAppointment(appointment) },
                    onExploreServices = {
                        post.businessId?.let { businessId ->
                            onNavigateToBookingFromProfile(businessId, provider.id, post.businessOwner.id)
                        }
                    }
                )
            }
        }
    }
}

// What this video-review is about: who was reviewed, what was said, and the services from that
// booking. The two possible next actions ("book too" / "browse all their services") live outside
// this composable, in VideoReviewActions, so they stay pinned below the scrollable content.
@Composable
private fun VideoReviewContent(
    modifier: Modifier = Modifier,
    review: PostReview,
    reviewer: PostUser,
    provider: ReviewedProvider,
    businessLocation: PostBusinessLocation?,
    appointment: Appointment,
    onNavigateToUserProfile: (UserProfileParam) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding),
        verticalArrangement = Arrangement.spacedBy(SpacingL)
    ) {
        ProviderCard(
            provider = provider,
            address = businessLocation?.formattedAddress,
            onNavigateToUserProfile = onNavigateToUserProfile
        )
        ReviewCard(review = review, reviewer = reviewer)
        ServicesCard(appointment = appointment)
    }
}

@Composable
private fun ProviderCard(
    provider: ReviewedProvider,
    address: String?,
    onNavigateToUserProfile: (UserProfileParam) -> Unit
) {
    SectionCard {
        Text(
            text = stringResource(R.string.reviewFor),
            style = bodySmall,
            color = Color.Gray
        )

        Spacer(Modifier.height(SpacingS))

        Row(verticalAlignment = Alignment.CenterVertically) {
            AvatarWithRating(
                url = provider.avatar ?: "",
                onClick = {
                    onNavigateToUserProfile(
                        UserProfileParam(provider.id, provider.username, provider.profession)
                    )
                },
                rating = provider.ratingsAverage,
                size = 65.dp
            )

            Spacer(Modifier.width(SpacingM))

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = provider.fullName,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${provider.profession} • ${provider.ratingsCount} ${stringResource(R.string.reviews)}",
                    style = bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (!address.isNullOrBlank()) {
            Spacer(Modifier.height(SpacingM))
            HorizontalDivider(color = Divider, thickness = 0.55.dp)
            Spacer(Modifier.height(SpacingM))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.ic_location_outline),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(IconSizeS)
                )

                Spacer(Modifier.width(SpacingS))

                Text(
                    text = address,
                    style = bodyMedium,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ReviewCard(review: PostReview, reviewer: PostUser) {
    SectionCard {
        Text(
            text = stringResource(R.string.review),
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(SpacingM))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Avatar(
                url = reviewer.avatar ?: "",
                size = AvatarSizeXXS
            )

            Spacer(Modifier.width(SpacingS))

            Text(
                text = reviewer.fullName,
                style = bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.height(SpacingS))

        Row(verticalAlignment = Alignment.CenterVertically) {
            RatingsStars(rating = review.rating.toFloat(), starSize = 18.dp)

            Spacer(Modifier.width(SpacingS))

            Text(
                text = "${review.rating} ${stringResource(R.string.from5)}",
                style = bodyMedium,
                color = Color.Gray
            )
        }

        Spacer(Modifier.height(SpacingS))

        Text(
            text = review.review,
            style = bodyLarge,
        )
    }
}

@Composable
private fun ServicesCard(appointment: Appointment) {
    SectionCard {
        Text(
            text = stringResource(R.string.bookedServices),
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(SpacingM))

        appointment.products.forEachIndexed { index, product ->
            val summary = listOf(product.getDurationText(), product.getFiltersSummary())
                .filter { it.isNotBlank() }
                .joinToString(" • ")

            AppointmentProductPrice(
                name = product.name,
                subtitle = summary,
                price = product.price,
                priceWithDiscount = product.priceWithDiscount,
                discount = product.discount,
                currencyName = product.currency.name
            )

            if (index < appointment.products.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = SpacingM),
                    color = Divider,
                    thickness = 0.55.dp
                )
            }
        }
    }
}

@Composable
private fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ShapeDefaults.Large)
            .background(SurfaceBG)
            .padding(BasePadding),
        content = content
    )
}

// Pinned below the scrollable content: the two things a viewer can actually do here — book the
// same services, or browse everything this business/employee offers. No price/total here, this
// isn't a checkout screen.
@Composable
private fun VideoReviewActions(
    modifier: Modifier = Modifier,
    provider: ReviewedProvider,
    onBook: () -> Unit,
    onExploreServices: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Background)
            .padding(horizontal = BasePadding, vertical = SpacingM)
    ) {
        HorizontalDivider(
            modifier = Modifier.padding(bottom = SpacingM),
            color = Divider,
            thickness = 0.55.dp
        )

        MainButton(
            title = "Rezerva aceleasi servicii",
            onClick = onBook,
            contentPadding = PaddingValues(14.dp)
        )

        Spacer(Modifier.height(SpacingS))

        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onExploreServices
        ) {
            Text(
                text = stringResource(R.string.seeAllServicesFrom, provider.fullName),
            )
        }
    }
}
