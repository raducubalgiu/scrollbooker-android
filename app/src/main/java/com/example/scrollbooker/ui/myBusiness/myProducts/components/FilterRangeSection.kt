package com.example.scrollbooker.ui.myBusiness.myProducts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.inputs.InputCheckbox
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.LastMinute
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun FilterRangeSection(
    filterName: String,
    minim: String,
    maxim: String,
    unit: String?,
    isNotApplicable: Boolean,
    onMinim: (String) -> Unit,
    onMaxim: (String) -> Unit,
    onIsApplicable: () -> Unit
) {
    Column {
        Text(
            text = filterName,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )

        Spacer(Modifier.height(SpacingS))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InputRange(
                modifier = Modifier.weight(0.5f),
                value = minim,
                onValueChange = onMinim,
                isEnabled = !isNotApplicable,
                label = "${stringResource(R.string.over)} ($unit)",
            )

            Spacer(Modifier.width(BasePadding))

            InputRange(
                modifier = Modifier.weight(0.5f),
                value = maxim,
                onValueChange = onMaxim,
                isEnabled = !isNotApplicable,
                label = "${stringResource(R.string.under)} ($unit)",
            )
        }

        Spacer(Modifier.height(SpacingS))

        InputCheckbox(
            modifier = Modifier.clip(shape = ShapeDefaults.Medium),
            height = 55.dp,
            checked = isNotApplicable,
            onCheckedChange = onIsApplicable,
            headLine = stringResource(R.string.itDoesntMatter)
        )
    }
}

@Composable
private fun InputRange(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isEnabled: Boolean,
    label: String
) {
    Input(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        isEnabled = isEnabled,
        label = label,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Background,
            unfocusedContainerColor = Background,
            cursorColor = LastMinute,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = LastMinute,
            unfocusedLabelColor = Color.Gray,
            focusedTextColor = OnBackground,
            unfocusedTextColor = OnBackground,
            disabledIndicatorColor = Color.Transparent,
            errorContainerColor = SurfaceBG,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType =  KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
    )
}