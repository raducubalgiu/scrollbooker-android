package com.example.scrollbooker.feature.profile.presentation.edit

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.headlineLarge

@Composable
fun EditProfessionScreen(
    viewModel: ProfileSharedViewModel,
    onBack: () -> Unit
) {
    val options = listOf(
        Option(
            value = "stylist",
            name = "Stylist"
        ),
        Option(
            value = "frizer",
            name = "Frizer"
        ),
        Option(
            value = "stylist",
            name = "Stylist"
        )
    )

    Layout(
        headerTitle = "",
        onBack = onBack
    ) {
        Text(
            text = stringResource(R.string.myProfession),
            style = headlineLarge,
            color = OnBackground,
            fontWeight = FontWeight.ExtraBold,
        )
        Text(text = "Poti alege profesia ta din lista de mai jos")

        Spacer(Modifier.height(SpacingXXL))

        InputRadio(
            options = options,
            value = "frizer",
            onValueChange = {}
        )
    }
}