package com.example.scrollbooker.ui.myBusiness.myClasses

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun MyClassesScreen(
    viewModel: MyClassesViewModel,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }

    Layout(
        onBack = onBack
    ) {
        Input(
            value = name,
            onValueChange = { name = it },
            label = "Numele clasei*",
        )

        Spacer(Modifier.height(BasePadding))

        Input(
            value = description,
            onValueChange = { description = it },
            label = "Descriere",
        )

        Spacer(Modifier.height(BasePadding))

        Input(
            value = capacity,
            onValueChange = { capacity = it },
            label = "Capacitate",
            keyboardOptions = KeyboardOptions(
                keyboardType =  KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
        )

        Spacer(Modifier.height(BasePadding))

        InputSelect(
            label = "Nivel dificultate",
            placeholder = "Selecteaza serviciul",
            options = listOf(
                Option(
                    value = "1",
                    name = "Incepatori"
                ),
                Option(
                    value = "2",
                    name = "Intermediar"
                ),
                Option(
                    value = "3",
                    name = "Avansat"
                )
            ),
            selectedOption = "",
            onValueChange = {}
        )

        Spacer(Modifier.height(BasePadding))

        InputSelect(
            label = "Serviciu",
            placeholder = "Selecteaza serviciul",
            options = listOf(
                Option(
                    value = "1",
                    name = "Bachata"
                ),
                Option(
                    value = "2",
                    name = "Salsa"
                ),
                Option(
                    value = "3",
                    name = "Avansat"
                )
            ),
            selectedOption = "",
            onValueChange = {}
        )
    }
}