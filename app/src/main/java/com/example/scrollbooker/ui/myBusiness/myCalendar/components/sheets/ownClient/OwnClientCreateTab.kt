package com.example.scrollbooker.ui.myBusiness.myCalendar.components.sheets.ownClient

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun OwnClientCreateTab(
    servicesState: FeatureState<List<Service>>,
    currenciesState: FeatureState<List<Currency>>,
    buttonHeightDp: Dp,
    ownClientState: OwnClientFormStateState,
    onEvent: (OwnClientFormEvent) -> Unit,
    validation: OwnClientValidationResult
) {
    var shouldSelectService by rememberSaveable { mutableIntStateOf(0) }
    var shouldSelectProduct by rememberSaveable { mutableIntStateOf(0) }

    val servicesOptionList = when(val state = servicesState) {
        is FeatureState.Success -> state.data.map { service ->
            Option(
                value = service.id.toString(),
                name = service.name
            )
        }
        else -> emptyList()
    }

    val currenciesOptionList = when(val state = currenciesState) {
        is FeatureState.Success -> state.data.map { service ->
            Option(
                value = service.id.toString(),
                name = service.name
            )
        }
        else -> emptyList()
    }

    val isServicesEnabled = servicesState is FeatureState.Success
    val isCurrencyEnabled = currenciesState is FeatureState.Success

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = buttonHeightDp + BasePadding)

    ) {
        Spacer(Modifier.padding(bottom = BasePadding))

        Input(
            label = "${stringResource(R.string.clientName)}*",
            value = ownClientState.customerName,
            onValueChange = {
                onEvent(OwnClientFormEvent.CustomerNameChanged(it))
            },
            isError = validation.customerNameError?.isNotBlank() == true,
            isInputValid = validation.customerNameError.isNullOrBlank(),
            errorMessage = validation.customerNameError,
            maxLength = validation.customerNameMaxLength
        )

        Spacer(Modifier.padding(bottom = BasePadding))

        Row {
            Column(Modifier.weight(0.8f)) {
                AnimatedContent(
                    targetState = shouldSelectService,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                ) { target ->
                    when(target) {
                        0 -> {
                            InputSelect(
                                label = "${stringResource(R.string.service)}*",
                                placeholder = "${stringResource(R.string.service)}*",
                                isEnabled = isServicesEnabled,
                                options = servicesOptionList,
                                selectedOption = ownClientState.selectedServiceId.toString(),
                                onValueChange = { onEvent(OwnClientFormEvent.ServiceSelected(it)) }
                            )
                        }

                        1 -> {
                            Input(
                                modifier = Modifier.weight(0.8f),
                                label = "${stringResource(R.string.service)}*",
                                value = ownClientState.serviceName,
                                onValueChange = { onEvent(OwnClientFormEvent.ServiceNameChanged(it)) },
                                maxLength = validation.serviceNameMaxLength
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.width(SpacingS))

            CustomIconButton(
                imageVector = if(shouldSelectService == 0) Icons.Default.Edit else Icons.Default.FormatListNumbered,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(SurfaceBG),
                onClick = { shouldSelectService = if(shouldSelectService == 0) 1 else 0 },
                tint = Color.Gray,
            )
        }

        Spacer(Modifier.padding(bottom = BasePadding))

        Row {
            Column(Modifier.weight(0.8f)) {
                AnimatedContent(
                    targetState = shouldSelectProduct,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                ) { target ->
                    when(target) {
                        0 -> {
                            InputSelect(
                                label = "${stringResource(R.string.product)}*",
                                placeholder = "${stringResource(R.string.product)}*",
                                options = listOf(
                                    Option(value = "1", "Tuns Special"),
                                    Option(value = "2", "Tuns Scurt"),
                                    Option(value = "3", "Tuns bros")
                                ),
                                selectedOption = ownClientState.selectedProductId.toString(),
                                onValueChange = { onEvent(OwnClientFormEvent.ProductSelected(it)) }
                            )
                        }

                        1 -> {
                            Input(
                                modifier = Modifier.weight(0.8f),
                                label = "${stringResource(R.string.product)}*",
                                value = ownClientState.productName,
                                onValueChange = { onEvent(OwnClientFormEvent.ProductNameChanged(it)) },
                                maxLength = validation.productNameMaxLength
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.width(SpacingS))

            CustomIconButton(
                imageVector = if(shouldSelectProduct == 0) Icons.Default.Edit else Icons.Default.FormatListNumbered,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(SurfaceBG),
                onClick = {  shouldSelectProduct = if(shouldSelectProduct == 0) 1 else 0  },
                tint = Color.Gray,
            )
        }

        Spacer(Modifier.padding(bottom = BasePadding))

        Input(
            label = "${stringResource(R.string.price)}*",
            value = ownClientState.price,
            onValueChange = { onEvent(OwnClientFormEvent.PriceChanged(it)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(Modifier.padding(bottom = BasePadding))

        Input(
            label = "${stringResource(R.string.priceWithDiscount)}*",
            value = ownClientState.priceWithDiscount,
            onValueChange = { onEvent(OwnClientFormEvent.PriceWithDiscountChanged(it)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(Modifier.padding(bottom = BasePadding))

        Input(
            label = "${stringResource(R.string.discount)}*",
            value = ownClientState.discount,
            onValueChange = { onEvent(OwnClientFormEvent.DiscountChanged(it)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(Modifier.padding(bottom = BasePadding))

        InputSelect(
            label = "${stringResource(R.string.currency)}*",
            placeholder = "${stringResource(R.string.currency)}*",
            options = currenciesOptionList,
            isEnabled = isCurrencyEnabled,
            selectedOption = ownClientState.selectedCurrencyId.toString(),
            onValueChange = { onEvent(OwnClientFormEvent.CurrencySelected(it)) }
        )
    }
}