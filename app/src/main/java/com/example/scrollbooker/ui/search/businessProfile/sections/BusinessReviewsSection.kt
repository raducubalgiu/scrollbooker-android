package com.example.scrollbooker.ui.search.businessProfile.sections
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileReview
import com.example.scrollbooker.ui.theme.headlineSmall
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun BusinessReviewsSection(
    reviews: List<BusinessProfileReview>,
    ratingsAverage: Float,
    ratingsCount: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(
                horizontal = BasePadding,
                vertical = SpacingXL
            ),
            text = stringResource(R.string.reviews),
            style = headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        RatingsStars(
            modifier = Modifier.padding(start = BasePadding),
            rating = ratingsAverage,
            maxRating = 5,
        )

        Spacer(Modifier.height(SpacingS))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BasePadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = ratingsAverage.toString(),
                style = titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.width(SpacingM))

            Text(
                text = "(${ratingsCount})",
                style = titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(SpacingXL))

        if(reviews.isEmpty()) {
            Text(
                modifier = Modifier.padding(start = BasePadding),
                text = "Inca nu au fost adaugate recenzii"
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