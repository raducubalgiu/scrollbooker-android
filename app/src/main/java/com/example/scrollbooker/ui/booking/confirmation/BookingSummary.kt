package com.example.scrollbooker.ui.booking.confirmation
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlowUser
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun BookingSummary(
    startDate: String,
    totalDuration: Int,
    owner: BookingFlowUser,
    address: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Divider, ShapeDefaults.Medium),
        colors = CardDefaults.cardColors(containerColor = SurfaceBG),
        shape = RoundedCornerShape(BasePadding)
    ) {
        BookingSummaryOwner(owner)

        HorizontalDivider(Modifier.padding(horizontal = BasePadding))

        BookingSummaryItem(
            title = stringResource(R.string.dateAndHour),
            description = "$startDate (${totalDuration}) min"
        )

        HorizontalDivider(Modifier.padding(horizontal = BasePadding))

        BookingSummaryItem(
            title = stringResource(R.string.location),
            description = address
        )
    }
}