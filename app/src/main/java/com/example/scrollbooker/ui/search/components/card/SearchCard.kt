package com.example.scrollbooker.ui.search.components.card

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun SearchCard(
    fullName: String,
    ratingsAverage: Float,
    ratingsCount: Int,
    profession: String,
    address: String,
    products: List<Product>,
    onNavigateToBusinessProfile: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pagerState = rememberPagerState { 3 }
    val images = listOf(
        "https://media.scrollbooker.ro/business-video-1-cover.jpeg",
        "https://media.scrollbooker.ro/business-video-1-cover.jpeg",
        "https://media.scrollbooker.ro/business-video-1-cover.jpeg"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .padding(horizontal = BasePadding)
            .clickable(
                onClick = onNavigateToBusinessProfile,
                interactionSource = interactionSource,
                indication = null
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            HorizontalPager(
                state = pagerState,
                overscrollEffect = null,
                beyondViewportPageCount = 0
            ) { page ->
                AsyncImage(
                    model = images[page],
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.35f)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(images.size) { index ->
                    val isSelected = pagerState.currentPage == index

                    val targetSize = if (isSelected) 7.dp else 6.dp
                    val dotSize by animateDpAsState(
                        targetValue = targetSize,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        ),
                        label = "dotSize"
                    )

                    val targetAlpha = if (isSelected) 0.8f else 0.4f
                    val dotAlpha by animateFloatAsState(
                        targetValue = targetAlpha,
                        animationSpec = tween(durationMillis = 180),
                        label = "dotAlpha"
                    )

                    Box(
                        modifier = Modifier
                            .size(dotSize)
                            .clip(CircleShape)
                            .background(
                                Color.White.copy(alpha = dotAlpha)
                            )
                    )
                }
            }

            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.2f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.4f)
                        )
                    )
                )
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = fullName,
                    style = titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    fontSize = 18.sp
                )

                Text(
                    text = profession,
                    style = bodyLarge,
                    color = Color.Gray,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SpacingXS)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_star_solid),
                    contentDescription = null,
                    tint = Primary
                )

                Text(
                    text = "$ratingsAverage",
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "(${ratingsCount})",
                    color = Color.Gray
                )
            }
        }

        Spacer(Modifier.height(SpacingS))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "4.5km",
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = "\u2022",
                color = Color.Gray
            )

            Text(
                modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                text = address,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.height(BasePadding))

        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            products.forEachIndexed { index, prod ->
                SearchCardProductRow(product = prod)

                if(index < products.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = SpacingS),
                        color = Divider,
                        thickness = 0.55.dp
                    )
                }
            }

            MainButtonOutlined(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = BasePadding),
                title = stringResource(R.string.seeAllServices),
                onClick = {}
            )
        }
    }
}