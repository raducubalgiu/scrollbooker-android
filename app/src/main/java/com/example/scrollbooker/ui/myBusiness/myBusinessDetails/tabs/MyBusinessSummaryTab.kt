package com.example.scrollbooker.ui.myBusiness.myBusinessDetails.tabs

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.components.customized.SectionMap
import com.example.scrollbooker.core.extensions.formatRating
import com.example.scrollbooker.core.util.Dimens.AvatarSizeM
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessDetails
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun MyBusinessSummaryTab(
    modifier: Modifier = Modifier,
    businessDetails: BusinessDetails
) {
    val owner = businessDetails.owner
    val location = businessDetails.location

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(BasePadding),
        verticalArrangement = Arrangement.spacedBy(SpacingXL)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceBG),
            shape = RoundedCornerShape(BasePadding)
        ) {
            Row(
                modifier = Modifier.padding(BasePadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(AvatarSizeM)
                        .clip(ShapeDefaults.Medium)
                ) {
                    AsyncImage(
                        model = owner.avatar,
                        contentDescription = "Owner Avatar",
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(Modifier.width(SpacingM))

                Column {
                    Text(
                        text = owner.fullName,
                        style = titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = owner.profession,
                        style = bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(SpacingXXS))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(SpacingXS)
                    ) {
                        Text(
                            text = owner.ratingsAverage.formatRating(),
                            fontWeight = FontWeight.Bold
                        )

                        RatingsStars(
                            rating = owner.ratingsAverage,
                            starSize = 18.dp
                        )

                        Text(
                            text = "(${owner.ratingsCount})",
                            color = Color.Gray
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = BasePadding),
                color = Divider,
                thickness = 0.55.dp
            )

            Row(
                modifier = Modifier.padding(BasePadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SpacingM)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Background, ShapeDefaults.Medium),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if(businessDetails.hasEmployees)
                            Icons.Outlined.PeopleOutline
                        else Icons.Outlined.Person,
                        contentDescription = null,
                        tint = Primary
                    )
                }

                Column {
                    Text(
                        text = stringResource(R.string.employees),
                        style = titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = stringResource(
                            if(businessDetails.hasEmployees) R.string.businessHasEmployeesLabel
                            else R.string.businessNoEmployeesLabel
                        ),
                        style = bodyMedium,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Column {
            Text(
                text = stringResource(R.string.address),
                style = titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(SpacingS))

            Text(
                text = location.formattedAddress,
                style = bodyMedium,
                color = Color.Gray
            )

            Spacer(Modifier.height(BasePadding))

            location.mapUrl?.let { mapUrl ->
                SectionMap(
                    mapUrl = mapUrl,
                    coordinates = location.coordinates,
                    fullName = owner.fullName
                )
            }
        }
    }
}
