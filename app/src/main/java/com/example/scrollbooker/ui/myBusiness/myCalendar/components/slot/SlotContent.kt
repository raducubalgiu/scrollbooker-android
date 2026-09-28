package com.example.scrollbooker.ui.myBusiness.myCalendar.components.slot
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.RoundCheckbox
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.availability.domain.model.BlockStatus
import com.example.scrollbooker.entity.booking.availability.domain.model.CalendarEventsSlot
import com.example.scrollbooker.entity.booking.availability.domain.model.blockStatus
import com.example.scrollbooker.entity.booking.availability.domain.model.isFreeSlot
import com.example.scrollbooker.ui.myBusiness.myCalendar.BlockUiState
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.bodySmall

@Composable
fun SlotContent(
    blockUiState: BlockUiState,
    slot: CalendarEventsSlot,
    lineColor: Color,
    height: Dp,
    isBefore: Boolean,
    onSlotClick: (CalendarEventsSlot) -> Unit,
) {
    val isCompact = height < 40.dp
    val isVeryCompact = height < 28.dp

    val defaultBlockedLocalDates = blockUiState.defaultBlockedLocalDates
    val blockedLocalDates = blockUiState.blockedLocalDates
    val blockStatus = slot.blockStatus(defaultBlockedLocalDates, localBlocked = blockedLocalDates)

    val isPermanentlyBlocked = blockStatus == BlockStatus.Permanent
    val isBlockedLocally = blockStatus == BlockStatus.Local

    val showCheckbox = (blockUiState.isBlocking && slot.isFreeSlot()) || isPermanentlyBlocked
    val isCheckboxEnabled = blockStatus != BlockStatus.Permanent
    val isCheckboxChecked = isBlockedLocally || isPermanentlyBlocked

    val isExternal = slot.info?.isExternal == true

    val showBookLine = (!slot.isFreeSlot() && !slot.isBlocked) || isExternal
    val blockedMessage = slot.info?.message ?: stringResource(R.string.blocked)

    val servicesNames = slot.info?.products
        ?.joinToString(" • ") { it.productName }
        .orEmpty()

    val price = slot.info?.totalPriceWithDiscount?.toTwoDecimals()
    val currencyName = slot.info?.paymentCurrency?.name

    val subtitle = listOfNotNull(
        servicesNames.takeIf { it.isNotBlank() },
        price?.let { if (currencyName != null) "$it $currencyName" else it }
    ).joinToString(" • ")

    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            if(showBookLine) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .clip(shape = ShapeDefaults.Large)
                        .background(lineColor)
                )

                Spacer(Modifier.width(SpacingS))
            }

            Box {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AnimatedVisibility(
                        visible = showCheckbox && isCheckboxEnabled,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            RoundCheckbox(
                                modifier = Modifier.align(Alignment.TopEnd),
                                checked = isCheckboxChecked,
                                borderColor = if (isCheckboxChecked) Error else Color.Gray,
                                fillColor = if (isCheckboxChecked) Error else Color.Transparent,
                                checkmarkColor = Color.White,
                                onCheckedChange = { onSlotClick(slot) },
                            )
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    val maxLines = if (isCompact) 1 else 2

                    if (!isVeryCompact) {
                        when {
                            slot.isBlocked -> {
                                SlotContainer(text = blockedMessage) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth().weight(1f),
                                        verticalArrangement = Arrangement.Bottom,
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            text = if(isExternal) "Din Google Calendar" else "Slot Blocat",
                                            style = bodySmall,
                                            color = if(isExternal) Color.Gray else lineColor
                                        )
                                    }
                                }
                            }

                            isCheckboxChecked -> {
                                SlotContainer(
                                    text = stringResource(R.string.blockInProgress),
                                    color = Error
                                )
                            }

                            blockUiState.isBlocking && slot.isFreeSlot() -> null

                            slot.isBooked -> {
                                SlotContainer(text = slot.info?.customer?.fullname ?: stringResource(R.string.booked)) {
                                    Text(
                                        text = subtitle,
                                        style = bodyMedium,
                                        maxLines = maxLines,
                                        overflow = TextOverflow.Ellipsis,
                                        color = Color.Gray
                                    )
                                }
                            }

                            slot.isLastMinute -> {
                                SlotIsLastMinute(
                                    title = "Last minute • ${slot.lastMinuteDiscount}%"
                                )
                            }

                            isBefore -> {
                                SlotContainer(
                                    text = stringResource(R.string.unbookedSlot),
                                    color = Divider
                                )
                            }

                            else -> {
                                val iconSize = (height * 0.6f).coerceIn(16.dp, 28.dp)

                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        modifier = Modifier.size(iconSize),
                                        painter = painterResource(R.drawable.ic_circle_plus_outline),
                                        contentDescription = null,
                                        tint = Divider
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SlotContainer(
    text: String,
    color: Color = OnBackground,
    content: (@Composable ColumnScope.() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            style = bodyMedium,
            color = color,
            text = text,
            fontWeight = FontWeight.SemiBold
        )

        content?.invoke(this)
    }
}
