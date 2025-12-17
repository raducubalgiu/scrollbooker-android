package com.example.scrollbooker.ui.myBusiness.myCalendar.components.slot
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.calendar.domain.model.BlockStatus
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.entity.booking.calendar.domain.model.blockStatus
import com.example.scrollbooker.entity.booking.calendar.domain.model.isFreeSlot
import com.example.scrollbooker.ui.myBusiness.myCalendar.BlockUiState
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.labelMedium

@Composable
fun SlotContent(
    blockUiState: BlockUiState,
    slot: CalendarEventsSlot,
    lineColor: Color,
    height: Dp,
    isBefore: Boolean,
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

    val showBookLine = !slot.isFreeSlot() && !isBefore && !slot.isBlocked
    val blockedMessage = slot.info?.message ?: stringResource(R.string.blocked)

    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            if(showBookLine) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .clip(shape = ShapeDefaults.ExtraLarge)
                        .background(lineColor)
                )

                Spacer(Modifier.width(SpacingS))
            }

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${slot.startDateLocale!!.toLocalTime()} - ${slot.endDateLocale!!.toLocalTime()}",
                        style = labelMedium,
                        maxLines = 1
                    )

                    AnimatedVisibility(
                        visible = showCheckbox && isCheckboxEnabled,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Checkbox(
                                modifier = Modifier.align(Alignment.TopEnd),
                                checked = isCheckboxChecked,
                                enabled = isCheckboxEnabled,
                                onCheckedChange = {},
                            )
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    if (!isVeryCompact) {
                        val title = slot.info?.customer?.fullname ?: stringResource(R.string.booked)
                        val maxLines = if (isCompact) 1 else 2

                        when {
                            slot.isBlocked -> {
                                SlotContainer(
                                    text = blockedMessage,
                                    color = lineColor
                                )
                            }

                            isCheckboxChecked -> {
                                SlotContainer(
                                    text = stringResource(R.string.blockInProgress),
                                    color = Error
                                )
                            }

                            blockUiState.isBlocking -> null

                            slot.isBooked -> SlotIsBooked(title, maxLines)

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
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        modifier = Modifier.size(40.dp),
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
private fun SlotContainer(text: String, color: Color) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            color = color,
            text = text
        )
    }
}