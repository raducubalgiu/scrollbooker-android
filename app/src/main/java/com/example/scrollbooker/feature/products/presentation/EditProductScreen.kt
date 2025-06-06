package com.example.scrollbooker.feature.products.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.components.core.MainButton
import com.example.scrollbooker.components.inputs.Input
import com.example.scrollbooker.components.inputs.InputSelect
import com.example.scrollbooker.components.inputs.Option
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun EditProductScreen(
    productId: Int,
    productName: String,
    onBack: () -> Unit,
    viewModel: ProductsViewModel
) {
    val focusManager = LocalFocusManager.current
    val moveFocusDown = focusManager.moveFocus(FocusDirection.Down)

    var name by remember { mutableStateOf("") }
    var serviceId by remember { mutableStateOf("1") }
    var description by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var price by remember { mutableIntStateOf(0) }
    var discount by remember { mutableIntStateOf(0) }
    var currencyId by remember { mutableStateOf("1") }

    val scrollState = rememberScrollState()

    val services = listOf(
        Option(
            value = "1",
            name = "Tuns"
        ),
        Option(
            value = "2",
            name = "Pensat"
        )
    )

    val currencies = listOf(
        Option(
            value = "1",
            name = "RON"
        ),
        Option(
            value = "2",
            name = "USD"
        ),
        Option(
            value = "3",
            name = "EUR"
        )
    )

    Layout(
        headerTitle = productName,
        onBack = onBack
    ) {
        Column(modifier = Modifier
            .fillMaxSize().padding(vertical = BasePadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
            ) {
                Input(
                    value = name,
                    onValueChange = { name = it },
                    label = stringResource(R.string.name)
                )
                Spacer(Modifier.height(BasePadding))
                InputSelect(
                    placeholder = stringResource(R.string.service),
                    options = services,
                    selectedOption = serviceId,
                    onValueChange = { serviceId = it.toString() }
                )
                Spacer(Modifier.height(BasePadding))
                Input(
                    value = description,
                    onValueChange = { description = it },
                    label = stringResource(R.string.description)
                )
                Spacer(Modifier.height(BasePadding))
                Input(
                    value = duration,
                    onValueChange = { duration = it },
                    label = stringResource(R.string.duration)
                )
                Spacer(Modifier.height(BasePadding))
                Input(
                    value = "100",
                    onValueChange = { price = it.toInt() },
                    keyboardOptions = KeyboardOptions(
                        keyboardType =  KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { moveFocusDown }),
                    label = stringResource(R.string.price)
                )
                Spacer(Modifier.height(BasePadding))
                Input(
                    value = "150",
                    enabled = false,
                    onValueChange = {},
                    keyboardOptions = KeyboardOptions(
                        keyboardType =  KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { moveFocusDown }),
                    label = stringResource(R.string.priceWithDiscount)
                )
                Spacer(Modifier.height(BasePadding))
                Input(
                    value = "50",
                    onValueChange = { discount = it.toInt() },
                    keyboardOptions = KeyboardOptions(
                        keyboardType =  KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { moveFocusDown }),
                    label = stringResource(R.string.discount)
                )
                Spacer(Modifier.height(BasePadding))
                InputSelect(
                    placeholder = stringResource(R.string.currency),
                    options = currencies,
                    selectedOption = currencyId,
                    onValueChange = { currencyId = it.toString() }
                )
            }

            MainButton(
                onClick = {},
                title = stringResource(R.string.edit)
            )
        }
    }
}