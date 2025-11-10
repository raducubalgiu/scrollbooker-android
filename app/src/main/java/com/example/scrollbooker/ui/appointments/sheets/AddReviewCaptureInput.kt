package com.example.scrollbooker.ui.appointments.sheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.inputs.EditInput
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.appointments.RatingReviewUpdate
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun AddReviewCaptureInput(
    selectedWrittenReview: RatingReviewUpdate?,
    onValueChange: (String) -> Unit,
    isSaving: Boolean,
    onCreateReview: () -> Unit
) {
    Text(
        style = headlineMedium,
        color = OnBackground,
        fontWeight = FontWeight.ExtraBold,
        text = stringResource(R.string.howWasYourExperience)
    )

    Spacer(Modifier.height(SpacingXXS))

    Text(
        style = bodyLarge,
        fontWeight = FontWeight.Normal,
        color = Color.Gray,
        text = stringResource(R.string.howWasYourExperienceDescription),
    )

    Spacer(Modifier.height(SpacingXL))

    Column(Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.yourReview),
            style = titleMedium
        )

        Spacer(Modifier.height(SpacingM))

        EditInput(
            value = selectedWrittenReview?.review ?: "",
            onValueChange = onValueChange,
            placeholder = stringResource(R.string.shareSomeDetailsAboutYourExperience),
            singleLine = false,
            minLines = 4,
            maxLines = 4,
            maxLength = 200
        )

        Spacer(Modifier.height(BasePadding))

        MainButton(
            modifier = Modifier.padding(bottom = BasePadding),
            title = stringResource(R.string.add),
            onClick = onCreateReview,
            enabled = !isSaving,
            isLoading = isSaving
        )
    }
}