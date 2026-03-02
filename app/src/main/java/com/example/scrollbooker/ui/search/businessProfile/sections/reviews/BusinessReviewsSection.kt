package com.example.scrollbooker.ui.search.businessProfile.sections.reviews
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileReviews
import com.example.scrollbooker.ui.theme.headlineSmall
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun BusinessReviewsSection(
    reviews: BusinessProfileReviews,
    ratingsAverage: Float,
    ratingsCount: Int,
    onNavigateToReviewerProfile: (Int) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = BasePadding)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = stringResource(R.string.reviews),
            style = titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(BasePadding))

        BusinessReviewsSummary(
            ratingsAverage = ratingsAverage,
            ratingsCount = ratingsCount
        )

        Spacer(Modifier.height(SpacingXL))

        if(reviews.data.isEmpty()) {
            Text(
                modifier = Modifier.padding(start = BasePadding),
                text = stringResource(R.string.notFoundReviews)
            )
        }

        BusinessReviewsList(
            reviews = reviews.data,
            onNavigateToReviewerProfile = onNavigateToReviewerProfile
        )

        if(reviews.total > reviews.data.size) {
            MainButtonOutlined(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(BasePadding),
                contentPadding = PaddingValues(
                    vertical = BasePadding
                ),
                shape = ShapeDefaults.Medium,
                title = stringResource(R.string.seeAllReviews),
                onClick = {  }
            )
        }

//
//        LazyRow(modifier = Modifier.padding(top = BasePadding)) {
//            item { Spacer(Modifier.width(BasePadding)) }
//
//            items(10) {
//                Box(
//                    modifier = Modifier
//                        .height(185.dp)
//                        .clip(ShapeDefaults.Small)
//                        .aspectRatio(9f / 12f)
//                        .background(
//                            brush = Brush.verticalGradient(
//                                colors = listOf(
//                                    Color.Black.copy(alpha = 0.1f),
//                                    Color.Transparent,
//                                    Color.Black.copy(alpha = 0.2f)
//                                )
//                            )
//                        )
//                ) {
//                    AsyncImage(
//                        model = "",
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier.fillMaxSize()
//                    )
//                }
//
//                Spacer(Modifier.width(SpacingM))
//            }
//
//            item { Spacer(Modifier.width(BasePadding)) }
//        }
    }
}