package com.example.scrollbooker.ui.shared.posts.sheets.bookings.tabs
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.ui.appointments.components.AppointmentProductPrice
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.headlineSmall
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

@Composable
fun ConfirmTab(
    totalPriceWithDiscount: BigDecimal,
    totalDuration: Int,
    products: Set<Product>,
    isSaving: Boolean,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(horizontal = BasePadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvatarWithRating(
                    modifier = Modifier.padding(
                        top = BasePadding,
                        bottom = SpacingXS
                    ),
                    url = "",
                    onClick = {},
                    rating = 4.5f,
                    size = 65.dp
                )

                Spacer(Modifier.width(SpacingM))

                Column {
                    Text(
                        text = "Radu Ion",
                        style = titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Frizerie",
                        style = bodyLarge,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.height(SpacingXL))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Luni, 10 nov, 15:00",
                    style = headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(SpacingM))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_clock_outline),
                        contentDescription = null,
                        tint = Color.Gray
                    )

                    Spacer(Modifier.width(SpacingS))

                    Text(
                        text = "($totalDuration min)",
                        style = bodyLarge,
                        color = Color.Gray
                    )
                }
            }

            Text(
                modifier = Modifier.padding(vertical = SpacingXL),
                text = "${stringResource(R.string.services)}:",
                style = titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            products.forEachIndexed { index, prod ->
                AppointmentProductPrice(
                    name = prod.name,
                    price = prod.price,
                    priceWithDiscount = prod.priceWithDiscount,
                    discount = prod.discount,
                    currencyName = "RON"
                )

                if(index < products.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = BasePadding),
                        color = Divider,
                        thickness = 0.55.dp
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = BasePadding),
                color = Divider,
                thickness = 0.55.dp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total de plata",
                    style = titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    text = "${totalPriceWithDiscount.toTwoDecimals()} RON",
                    style = titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(Modifier.height(SpacingM))

            Text(
                text = "*Plata se va efectua la locatie in numerar sau in modalitatile de plata acceptate",
                style = bodySmall,
                color = Color.Gray
            )
        }

        Column {
            HorizontalDivider(color = Divider, thickness = 0.55.dp)
            MainButton(
                modifier = Modifier.padding(BasePadding),
                title = stringResource(R.string.book),
                enabled = !isSaving,
                isLoading = isSaving,
                onClick = onSave
            )
        }
    }
}