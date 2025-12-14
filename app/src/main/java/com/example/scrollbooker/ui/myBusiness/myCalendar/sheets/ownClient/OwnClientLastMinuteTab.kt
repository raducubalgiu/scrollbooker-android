package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineMedium
import com.example.scrollbooker.ui.theme.headlineSmall
import com.example.scrollbooker.ui.theme.titleMedium
import java.math.BigDecimal

@Composable
fun OwnClientLastMinuteTab(
    slotLabel: String,
    buttonHeightDp: Dp,
    lastMinuteDiscount: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                top = BasePadding,
                bottom = buttonHeightDp + BasePadding,
                start = BasePadding,
                end = BasePadding
            )
    ) {
        Text(
            style = headlineMedium,
            color = OnBackground,
            fontWeight = FontWeight.ExtraBold,
            text = stringResource(R.string.configureLastMinute)
        )
        Spacer(Modifier.height(SpacingXXS))
        Text(
            style = bodyLarge,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
            text = stringResource(R.string.configureLastMinuteDescription),
        )

        Spacer(Modifier.height(SpacingXL))

        Text(
            text = stringResource(R.string.selectedSlot),
            style = titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(SpacingS))

        Text(
            text = slotLabel,
            style = headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(SpacingXL))

        Input(
            label = stringResource(R.string.discountForThisSlot),
            value = lastMinuteDiscount,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
    }
}