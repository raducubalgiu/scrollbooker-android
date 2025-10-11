package com.example.scrollbooker.ui.onboarding.business

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.EditInput
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.checkLength
import com.example.scrollbooker.ui.myBusiness.myBusinessLocation.MyBusinessLocationViewModel
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun CollectBusinessDetailsScreen(
    viewModel: MyBusinessLocationViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val name by viewModel.currentName.collectAsState()
    val description by viewModel.currentDescription.collectAsState()

    val fullNameMinLength = 3
    val fullNameMaxLength = 35
    val descriptionMaxLength = 255

    val checkName = checkLength(
        LocalContext.current, name, minLength=fullNameMinLength, maxLength=fullNameMaxLength
    )
    val isNameValid = checkName.isNullOrBlank()

    val checkDescription = checkLength(LocalContext.current, description, maxLength = descriptionMaxLength)
    val isDescriptionValid = checkDescription.isNullOrBlank()

    val isEnabled = !name.isEmpty() && isNameValid && isDescriptionValid

    FormLayout(
        headLine = stringResource(R.string.locationPresentation),
        subHeadLine = stringResource(R.string.locationPresentationDescription),
        onBack = onBack,
        onNext = onNext,
        buttonTitle = stringResource(R.string.nextStep),
        isEnabled = isEnabled
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SpacingXXL)
        ) {
            Spacer(Modifier.height(BasePadding))

            Text(
                text = "${stringResource(R.string.name)}*",
                style = titleMedium,
                fontWeight = FontWeight.Bold,
            )
            EditInput(
                value = name,
                placeholder = stringResource(R.string.yourBusinessName),
                onValueChange = { viewModel.setBusinessName(it) },
                isEnabled = true,
                isError = !isNameValid,
                errorMessage = checkName,
                maxLength = fullNameMaxLength
            )

            Spacer(Modifier.height(SpacingXXL))

            Text(
                text = stringResource(R.string.description),
                style = titleMedium,
                fontWeight = FontWeight.Bold,
            )
            EditInput(
                value = description,
                placeholder = stringResource(R.string.addDescription),
                onValueChange = { viewModel.setBusinessDescription(it) },
                singleLine = false,
                minLines = 5,
                maxLines = 5,
                isError = !isDescriptionValid,
                errorMessage = checkDescription,
                maxLength = descriptionMaxLength
            )
        }
    }
}