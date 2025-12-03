package com.example.scrollbooker.ui.search.sheets
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG

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
            modifier = Modifier
                .weight(0.5f)
                .border(width = 1.dp, Divider, shape = ShapeDefaults.Small),
            shape = ShapeDefaults.Small,
            title = stringResource(R.string.delete),
            onClick = onClear,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = OnSurfaceBG
            )
        )

        Spacer(Modifier.width(SpacingM))

        MainButton(
            modifier = Modifier.weight(0.5f),
            shape = ShapeDefaults.Small,
            title = stringResource(R.string.confirm),
            onClick = onConfirm
        )
    }

    Spacer(Modifier.height(BasePadding))
}