package com.example.scrollbooker.ui.myBusiness.myCalendar.components.Slot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.enums.AppointmentChannelEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.entity.booking.calendar.domain.model.toTime
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun CalendarBookedSlot(slot: CalendarEventsSlot) {
    val isOwnClient = slot.info?.channel == AppointmentChannelEnum.OWN_CLIENT
    //val product = slot.info?.product
    val customer = slot.info?.customer

    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            if(isOwnClient) Color.Blue.copy(alpha = 0.1f)
            else Primary.copy(alpha = 0.1f)
        )
        .padding(BasePadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = slot.toTime().start,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                modifier = Modifier.padding(horizontal = SpacingS),
                text = "-",
                fontWeight = FontWeight.SemiBold
            )
            Text(
                modifier = Modifier.padding(end = SpacingM),
                text = slot.toTime().end,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "(30 de minute)",
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(BasePadding))

        customer?.let {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(size = 35.dp, url = "")
                Spacer(Modifier.width(SpacingS))
                Text(
                    text = it.fullname,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(Modifier.height(BasePadding))

//        product?.let {
//            Text(
//                text = it.productName,
//                style = titleMedium
//            )
//
//            Spacer(Modifier.height(SpacingS))
//
//            Row {
//                Text(
//                    text = it.productPriceWithDiscount.toString(),
//                    style = titleMedium,
//                    fontWeight = FontWeight.Bold
//                )
//                Spacer(Modifier.width(SpacingS))
//                Text(
//                    text = slot.info.currency?.name ?: "",
//                    style = titleMedium,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
    }
}