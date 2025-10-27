package com.example.scrollbooker.ui.search.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.slider.CustomSlider
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun SearchDistanceSheet(
    viewModel: SearchViewModel,
    onClose: () -> Unit
) {
    SearchSheetsHeader(
        title = stringResource(R.string.distance),
        onClose = onClose
    )

    Spacer(Modifier.height(BasePadding))

    var value by remember { mutableFloatStateOf(1500f) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Distanta maxima",
            fontWeight = FontWeight.SemiBold,
            style = titleMedium
        )

        Text(
            text = "${value.toInt()} lm",
            style = bodyLarge
        )
    }

    CustomSlider(
        modifier = Modifier.padding(SpacingXL),
        value = value,
        onValueChange = { value = it },
        valueRange = 0f..1500f
    )

    Spacer(Modifier.height(SpacingXL))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MainButton(
            modifier = Modifier.weight(0.5f),
            title = stringResource(R.string.cancel),
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = SurfaceBG,
                contentColor = OnSurfaceBG
            )
        )

        Spacer(Modifier.width(SpacingS))

        MainButton(
            modifier = Modifier.weight(0.5f),
            title = "Confirma",
            onClick = {}
        )
    }

    Spacer(Modifier.height(BasePadding))
}