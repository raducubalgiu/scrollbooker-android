package com.example.scrollbooker.ui.shared.posts.sheets.bookings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.BookingStepper
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.headlineSmall

@Composable
fun BookingSheetHeader(
    stepTitle: String,
    onClose: () -> Unit,
    totalSteps: Int,
    currentStep: Int
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomIconButton(
                boxSize = 60.dp,
                imageVector = Icons.Default.Close,
                tint = Color.Transparent,
                onClick = {}
            )

            BookingStepper(
                totalSteps = totalSteps,
                currentStep = currentStep
            )

            CustomIconButton(
                boxSize = 60.dp,
                imageVector = Icons.Default.Close,
                tint = OnBackground,
                onClick = onClose
            )
        }

        Spacer(Modifier.height(SpacingM))

        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = stepTitle,
            style = headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(BasePadding))
    }
}