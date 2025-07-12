package com.example.scrollbooker.screens.search.businessProfile.tabs
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.headlineMedium
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun BusinessReviewsTab() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(SpacingXL))

        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = stringResource(R.string.reviews),
            style = headlineMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(SpacingXL))

        RatingsStars(
            modifier = Modifier.padding(start = BasePadding),
            rating = 4.5f,
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
                text = "4.5",
                style = titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.width(SpacingM))

            Text(
                text = "(1,000)",
                style = titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(SpacingXL))

        LazyRow(modifier = Modifier
            .padding(top = BasePadding)
        ) {
            item { Spacer(Modifier.width(BasePadding)) }

            items(10) {
                Box(
                    modifier = Modifier
                        .height(185.dp)
                        .clip(ShapeDefaults.Small)
                        .aspectRatio(9f / 12f)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.1f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.2f)
                                )
                            )
                        )
                ) {
                    AsyncImage(
                        model = "",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(Modifier.width(SpacingM))
            }

            item { Spacer(Modifier.width(BasePadding)) }
        }

        Spacer(Modifier.height(SpacingXL))
    }
}