package com.example.scrollbooker.ui.myBusiness.myCalendar.components.sheets.ownClient

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun OwnClientCreateTab(
    buttonHeightDp: Dp,
    ownClientState: OwnClientFormStateState,
    onEvent: (OwnClientFormEvent) -> Unit
) {
    var shouldSelectService by rememberSaveable { mutableStateOf(true) }
    var shouldSelectProduct by rememberSaveable { mutableStateOf(true) }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = buttonHeightDp + BasePadding)

    ) {
        Spacer(Modifier.padding(bottom = BasePadding))

        Input(
            label = "Numele clientului",
            value = ownClientState.customerName,
            onValueChange = { onEvent(OwnClientFormEvent.CustomerNameChanged(it)) }
        )

        Spacer(Modifier.padding(bottom = BasePadding))

        Row {
            if(shouldSelectService) {
                Column(Modifier.weight(0.8f)) {
                    InputSelect(
                        label = "Numele serviciului",
                        placeholder = "Selecteaza un serviciu",
                        options = listOf(
                            Option(value = "1", "Tuns"),
                            Option(value = "2", "Vopsit"),
                            Option(value = "3", "Pensat")
                        ),
                        selectedOption = ownClientState.selectedServiceId.toString(),
                        onValueChange = { onEvent(OwnClientFormEvent.ServiceSelected(it)) }
                    )
                }
            } else {
                Input(
                    modifier = Modifier.weight(0.8f),
                    label = "Numele serviciului",
                    value = ownClientState.serviceName,
                    onValueChange = { onEvent(OwnClientFormEvent.ServiceNameChanged(it)) }
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
                        label = "Numele produsului",
                        placeholder = "Selecteaza un produs",
                        options = listOf(
                            Option(value = "1", "Tuns Special"),
                            Option(value = "2", "Tuns Scurt"),
                            Option(value = "3", "Tuns bros")
                        ),
                        selectedOption = ownClientState.selectedProductId.toString(),
                        onValueChange = { onEvent(OwnClientFormEvent.ProductSelected(it)) }
                    )
                }
            } else {
                Input(
                    modifier = Modifier.weight(0.8f),
                    label = "Numele produsului",
                    value = ownClientState.productName,
                    onValueChange = { onEvent(OwnClientFormEvent.ProductNameChanged(it)) }
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
            value = ownClientState.price,
            onValueChange = { onEvent(OwnClientFormEvent.PriceChanged(it)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(Modifier.padding(bottom = BasePadding))

        Input(
            label = "Pret cu discount",
            value = ownClientState.priceWithDiscount,
            onValueChange = { onEvent(OwnClientFormEvent.PriceWithDiscountChanged(it)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(Modifier.padding(bottom = BasePadding))

        Input(
            label = "Discount",
            value = ownClientState.discount,
            onValueChange = { onEvent(OwnClientFormEvent.DiscountChanged(it)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(Modifier.padding(bottom = BasePadding))

        InputSelect(
            options = listOf(
                Option(value = "1", name = "RON"),
                Option(value = "2", name = "EUR")
            ),
            selectedOption = ownClientState.selectedCurrencyId.toString(),
            onValueChange = { onEvent(OwnClientFormEvent.CurrencySelected(it)) }
        )
    }
}