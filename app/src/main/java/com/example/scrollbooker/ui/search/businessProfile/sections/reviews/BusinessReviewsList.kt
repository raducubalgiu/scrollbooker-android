package com.example.scrollbooker.ui.search.businessProfile.sections.reviews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.extensions.display
import com.example.scrollbooker.core.util.Dimens.AvatarSizeM
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileReview
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun BusinessReviewsList(
    reviews: List<BusinessProfileReview>,
    onNavigateToReviewerProfile: (Int) -> Unit
) {
    reviews.forEachIndexed { index, r ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BasePadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(
                    url = r.reviewer.avatar ?: "",
                    size = AvatarSizeM,
                    onClick = { onNavigateToReviewerProfile(r.reviewer.id) }
                )

                Spacer(Modifier.width(BasePadding))

                Column {
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = SpacingXXS),
                        style = titleMedium,
                        fontSize = 18.sp,
                        color = OnBackground,
                        text = r.reviewer.fullName
                    )

                    Spacer(Modifier.height(SpacingXS))

                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = bodyMedium,
                        text = r.createdAt.display(),
                        color = Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(BasePadding))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RatingsStars(
                    rating = r.rating.toFloat(),
                    maxRating = 5,
                    starSize = 20.dp
                )

                Text(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    text = "\u2022",
                    color = Color.Gray
                )

                Text(
                    text = "${r.rating} din 5",
                    color = Color.Gray,
                    style = bodyMedium
                )
            }

            Spacer(Modifier.height(SpacingS))

            if(r.review.isNotEmpty()) {
                Text(text = r.review)
            }

            if(index < reviews.size - 1) {
                Spacer(Modifier.height(SpacingXXL))
            }
        }
    }
}