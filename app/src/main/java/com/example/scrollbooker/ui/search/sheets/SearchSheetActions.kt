package com.example.scrollbooker.ui.search.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun SearchSheetActions(
    onClear: () -> Unit,
    onConfirm: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MainButton(
            modifier = Modifier.weight(0.5f),
            title = stringResource(R.string.delete),
            onClick = onClear,
            colors = ButtonDefaults.buttonColors(
                containerColor = SurfaceBG,
                contentColor = OnSurfaceBG
            )
        )

        Spacer(Modifier.width(SpacingS))

        MainButton(
            modifier = Modifier.weight(0.5f),
            title = stringResource(R.string.confirm),
            onClick = onConfirm
        )
    }

    Spacer(Modifier.height(BasePadding))
}