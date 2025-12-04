package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.ProductCard
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun SearchCard(
    fullName: String,
    ratingsAverage: Float,
    ratingsCount: Int,
    address: String,
    products: List<Product>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .padding(horizontal = BasePadding)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = "https://media.scrollbooker.ro/business-video-1-cover.jpeg",
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )
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

        Spacer(Modifier.height(8.dp))

        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = fullName,
                    style = titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    fontSize = 18.sp
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_star_solid),
                        contentDescription = null,
                        tint = Primary
                    )

                    Text(
                        modifier = Modifier.padding(start = SpacingS, end = SpacingXXS),
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

            Text(
                modifier = Modifier.fillMaxWidth(fraction = 0.6f),
                text = address,
                color = Color.Gray,
            )

            Spacer(Modifier.height(8.dp))

            products.mapIndexed { index, prod ->
                ProductCard(
                    product = prod,
                    displayActions = false,
                    isSelected = false,
                    onSelect = {}
                )

                if(index < products.size - 1) {
                    HorizontalDivider(
                        color = Divider,
                        thickness = 0.55.dp
                    )
                }
            }
        }
    }
}