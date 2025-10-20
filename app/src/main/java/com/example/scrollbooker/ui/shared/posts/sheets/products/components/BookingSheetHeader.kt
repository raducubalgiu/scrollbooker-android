package com.example.scrollbooker.ui.shared.posts.sheets.products.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.shared.posts.sheets.products.BookingStepper
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun BookingSheetHeader(
    stepTitle: String,
    fullName: String,
    avatar: String,
    profession: String,
    onClose: () -> Unit,
    totalSteps: Int,
    currentStep: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(BasePadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Avatar(url = avatar)

            Spacer(Modifier.width(SpacingS))

            Column {
                Text(
                    text = fullName,
                    style = titleMedium,
                    fontSize = 18.sp
                )

                Spacer(Modifier.height(SpacingS))

                Text(
                    text = profession,
                    style = bodyLarge,
                    color = Color.Gray
                )
            }
        }

        CustomIconButton(
            imageVector = Icons.Default.Close,
            tint = OnBackground,
            onClick = onClose
        )
    }

    Spacer(Modifier.height(SpacingM))

    BookingStepper(
        totalSteps = totalSteps,
        currentStep = currentStep
    )

    Spacer(Modifier.height(BasePadding))

    Text(
        modifier = Modifier.padding(horizontal = BasePadding),
        text = stepTitle,
        style = headlineMedium,
        fontWeight = FontWeight.SemiBold
    )

    Spacer(Modifier.height(BasePadding))
}