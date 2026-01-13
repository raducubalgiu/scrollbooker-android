package com.example.scrollbooker.ui.myBusiness.myBusinessLocation.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMediaPreview
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessOwner
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessSheet
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.SubFilter
import com.example.scrollbooker.ui.search.components.card.SearchCard
import java.math.BigDecimal

@Composable
fun MyBusinessGalleryTab(
    onNavigateToEditGallery: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.weight(1f)) {
            SearchCard(
                modifier = Modifier.padding(top = BasePadding),
                business = BusinessSheet(
                    owner = BusinessOwner(
                        id = 1,
                        fullName = "Walter Studio",
                        username = "walter_studio",
                        avatar = "",
                        profession = "Frizerie",
                        ratingsAverage = 4.5f,
                        ratingsCount = 100
                    ),
                    businessShortDomain = "Beauty",
                    address = "Strada Oarecare, nr 31",
                    coordinates = BusinessCoordinates(
                        lat = 45.234f,
                        lng = 25.234f
                    ),
                    hasVideo = false,
                    mediaPreview = BusinessMediaPreview(
                        type = "photo",
                        thumbnailUrl = "",
                        previewVideoUrl = ""
                    ),
                    products = listOf(
                        Product(
                            id = 1,
                            name = "Serviciul 1",
                            description = "",
                            duration = 30,
                            price = BigDecimal(100),
                            priceWithDiscount = BigDecimal(100),
                            discount = BigDecimal(0),
                            userId = 1,
                            serviceId = 1,
                            businessId = 1,
                            currencyId = 1,
                            subFilters = emptyList(),
                            canBeBooked = true
                        ),
                        Product(
                            id = 1,
                            name = "Serviciul 2",
                            description = "",
                            duration = 30,
                            price = BigDecimal(100),
                            priceWithDiscount = BigDecimal(100),
                            discount = BigDecimal(0),
                            userId = 1,
                            serviceId = 1,
                            businessId = 1,
                            currencyId = 1,
                            subFilters = listOf(SubFilter(id=1, name = "Dummy")),
                            canBeBooked = true
                        ),
                        Product(
                            id = 1,
                            name = "Serviciul 3",
                            description = "",
                            duration = 30,
                            price = BigDecimal(100),
                            priceWithDiscount = BigDecimal(100),
                            discount = BigDecimal(0),
                            userId = 1,
                            serviceId = 1,
                            businessId = 1,
                            currencyId = 1,
                            subFilters = listOf(SubFilter(id=1, name = "Dummy")),
                            canBeBooked = true
                        )
                    )
                ),
                onNavigateToBusinessProfile = {},
                onOpenBookingsSheet = {},
                showMoreProductsBtn = false
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainButton(
                modifier = Modifier.padding(BasePadding),
                onClick = onNavigateToEditGallery,
                title = stringResource(R.string.editImages)
            )
        }
    }
}