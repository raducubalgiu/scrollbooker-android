package com.example.scrollbooker.screens.auth.collectClientDetails
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun CollectClientBirthDateScreen(
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    FormLayout(
        headLine = stringResource(R.string.dateOfBirth),
        subHeadLine = stringResource(R.string.dateOfBirthLabelDescription),
        buttonTitle = stringResource(R.string.nextStep),
        onBack = onBack,
        onNext = onNext
    ) {
        Row(
            modifier = Modifier.padding(horizontal = SpacingXL),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(0.3f)) {
                InputSelect(
                    options = listOf(Option(value = "23", name = "23")),
                    placeholder = "Ziua",
                    selectedOption = "23",
                    onValueChange = {}
                )
            }
            Spacer(Modifier.width(SpacingS))

            Column(Modifier.weight(0.35f)) {
                InputSelect(
                    options = listOf(Option(value = "Ianuarie", name = "Ianuarie")),
                    placeholder = "Luna",
                    selectedOption = "Ianuarie",
                    onValueChange = {}
                )
            }
            Spacer(Modifier.width(SpacingS))

            Column(Modifier.weight(0.35f)) {
                InputSelect(
                    options = listOf(Option(value = "1990", name = "1990")),
                    placeholder = "Anul",
                    selectedOption = "1990",
                    onValueChange = {}
                )
            }
        }

        Spacer(Modifier.height(SpacingXXL))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(
                onClick = {}
            ) {
                Text(
                    text = stringResource(R.string.preferNotToSay),
                    style = bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}