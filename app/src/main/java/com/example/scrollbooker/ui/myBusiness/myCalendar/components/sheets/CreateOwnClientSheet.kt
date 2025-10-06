package com.example.scrollbooker.ui.myBusiness.myCalendar.components.sheets

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.parseDateStringFromLocalDateTimeString
import com.example.scrollbooker.core.util.parseTimeStringFromLocalDateTimeString
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentOwnClientCreate
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.labelLarge
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOwnClientSheet(
    sheetState: SheetState,
    paddingTop: Dp,
    selectedOwnClientSlot: CalendarEventsSlot?,
    slotDuration: Int,
    onCreateOwnClient: (AppointmentOwnClientCreate) -> Unit,
    onClose: () -> Unit
) {
    val verticalScroll = rememberScrollState()
    var selectedIndex by remember { mutableIntStateOf(0) }

    var customerName by rememberSaveable { mutableStateOf("") }
    var serviceName by rememberSaveable { mutableStateOf("") }
    var productName by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var priceWithDiscount by rememberSaveable { mutableStateOf("") }
    var discount by rememberSaveable { mutableStateOf("0") }

    var shouldSelectService by rememberSaveable { mutableStateOf(true) }
    var shouldSelectProduct by rememberSaveable { mutableStateOf(true) }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {},
        containerColor = Background,
        dragHandle = {}
    ) {
        val tabs = listOf("Client propriu", "Last Minute")

        val startLocalDate = parseDateStringFromLocalDateTimeString(
            selectedOwnClientSlot?.startDateLocale
        )
        val startLocalTime = parseTimeStringFromLocalDateTimeString(
            selectedOwnClientSlot?.startDateLocale
        )

        val endLocalTime = parseTimeStringFromLocalDateTimeString(
            selectedOwnClientSlot?.endDateLocale
        )

        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val targetHeight = maxHeight - paddingTop

            val density = LocalDensity.current
            var buttonHeightPx by remember { mutableIntStateOf(0) }
            val buttonHeightDp = with(density) { buttonHeightPx.toDp() }

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(targetHeight)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = SpacingXL)
                ) {
                    SheetHeader(
                        customTitle = {
                            Column(modifier = Modifier.padding(BasePadding),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Administreaza intervalul",
                                    style = titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp
                                )

                                Spacer(Modifier.height(SpacingXS))

                                Text(
                                    text = "$startLocalDate \u2022 $startLocalTime - $endLocalTime",
                                    style = titleMedium,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Gray
                                )
                            }
                        },
                        onClose = onClose,
                        padding = 0.dp
                    )

                    Spacer(Modifier.height(BasePadding))

                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(SurfaceBG)
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                    ) {
                        SingleChoiceSegmentedButtonRow(
                            modifier = Modifier.fillMaxWidth(),
                            space = 0.dp
                        ) {
                            tabs.forEachIndexed { index, title ->
                                val isSelected = index == selectedIndex

                                SegmentedButton(
                                    selected = isSelected,
                                    onClick = { selectedIndex = index },
                                    icon = {},
                                    shape = RoundedCornerShape(50),
                                    colors = SegmentedButtonDefaults.colors(
                                        activeContainerColor = Background,
                                        activeContentColor = OnBackground,
                                        activeBorderColor = Background,
                                        inactiveContainerColor = SurfaceBG,
                                        inactiveContentColor = OnSurfaceBG,
                                        inactiveBorderColor = SurfaceBG,
                                    )
                                ) {
                                    Text(
                                        text = title,
                                        style = labelLarge,
                                        fontWeight = if(isSelected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }

                    Column(
                        Modifier
                            .weight(1f)
                            .verticalScroll(verticalScroll)
                            .padding(bottom = buttonHeightDp + BasePadding)
                    ) {
                        Spacer(Modifier.padding(bottom = BasePadding))

                        Input(
                            label = "Numele clientului",
                            value = customerName,
                            onValueChange = { customerName = it }
                        )

                        Spacer(Modifier.padding(bottom = BasePadding))

                        Row {
                            if(shouldSelectService) {
                                Column(Modifier.weight(0.8f)) {
                                    InputSelect(
                                        options = listOf(
                                            Option(value = "1", "Tuns"),
                                            Option(value = "2", "Vopsit"),
                                            Option(value = "3", "Pensat")
                                        ),
                                        selectedOption = "1",
                                        onValueChange = {  }
                                    )
                                }
                            } else {
                                Input(
                                    modifier = Modifier.weight(0.8f),
                                    label = "Numele serviciului",
                                    value = serviceName,
                                    onValueChange = { serviceName = it }
                                )
                            }

                            Spacer(Modifier.width(SpacingS))

                            CustomIconButton(
                                imageVector = if(shouldSelectService) Icons.Default.Edit else Icons.Default.FormatListNumbered,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceBG),
                                onClick = { shouldSelectService = !shouldSelectService },
                                tint = Color.Gray,
                            )
                        }

                        Spacer(Modifier.padding(bottom = BasePadding))

                        Row {
                            if(shouldSelectProduct) {
                                Column(Modifier.weight(0.8f)) {
                                    InputSelect(
                                        options = listOf(
                                            Option(value = "1", "Tuns Special"),
                                            Option(value = "2", "Tuns Scurt"),
                                            Option(value = "3", "Tuns bros")
                                        ),
                                        selectedOption = "1",
                                        onValueChange = {}
                                    )
                                }
                            } else {
                                Input(
                                    modifier = Modifier.weight(0.8f),
                                    label = "Numele produsului",
                                    value = serviceName,
                                    onValueChange = { serviceName = it }
                                )
                            }

                            Spacer(Modifier.width(SpacingS))

                            CustomIconButton(
                                imageVector = if(shouldSelectProduct) Icons.Default.Edit else Icons.Default.FormatListNumbered,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceBG),
                                onClick = { shouldSelectProduct = !shouldSelectProduct },
                                tint = Color.Gray,
                            )
                        }

                        Spacer(Modifier.padding(bottom = BasePadding))

                        Input(
                            label = "Pret",
                            value = price,
                            onValueChange = { price = it }
                        )

                        Spacer(Modifier.padding(bottom = BasePadding))

                        Input(
                            label = "Pret cu discount",
                            value = priceWithDiscount,
                            onValueChange = { priceWithDiscount = it }
                        )

                        Spacer(Modifier.padding(bottom = BasePadding))

                        Input(
                            label = "Discount",
                            value = discount,
                            onValueChange = { discount = it }
                        )

                        Spacer(Modifier.padding(bottom = BasePadding))

                        InputSelect(
                            options = listOf(
                                Option(value = "1", name = "RON"),
                                Option(value = "2", name = "EUR")
                            ),
                            selectedOption = "1",
                            onValueChange = {}
                        )
                    }
                }

                Box(
                    Modifier
                        .background(Background)
                        .zIndex(5f)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .imePadding()
                        .navigationBarsPadding()
                        .onGloballyPositioned {
                            buttonHeightPx = it.size.height // pentru padding-ul din body
                        }
                ) {
                    HorizontalDivider(color = Divider, thickness = 0.55.dp)
                    MainButton(
                        modifier = Modifier.padding(BasePadding),
                        title = "Adaugă",
                        onClick = {
                            selectedOwnClientSlot?.let { slot ->
                                onCreateOwnClient(AppointmentOwnClientCreate(
                                    startDate = slot.startDateUtc,
                                    endDate = slot.endDateUtc,
                                    customerFullname = customerName,
                                    serviceId = null,
                                    serviceName = serviceName,
                                    productId = null,
                                    productName = productName,
                                    productPrice = BigDecimal(price),
                                    productPriceWithDiscount = BigDecimal(priceWithDiscount),
                                    productDiscount = BigDecimal(discount),
                                    productDuration = slotDuration,
                                    currencyId = 1
                                ))
                            }
                        }
                    )
                }
            }
        }
    }
}