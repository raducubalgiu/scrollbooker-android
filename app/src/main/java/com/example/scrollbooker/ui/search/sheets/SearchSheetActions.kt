package com.example.scrollbooker.ui.search.sheets
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun SearchSheetActions(
    onClear: () -> Unit,
    onConfirm: () -> Unit,
    isClearEnabled: Boolean = true,
    isConfirmEnabled: Boolean = true,
    displayIcon: Boolean = true,
    primaryActionText: Int = R.string.search
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(BasePadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            TextButton(
                onClick = onClear,
                enabled = isClearEnabled,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = OnBackground,
                )
            ) {
                Text(
                    text = stringResource(R.string.delete),
                    style = titleMedium,
                )
            }
        }

        Button(
            contentPadding = PaddingValues(
                vertical = BasePadding,
                horizontal = SpacingXXL
            ),
            onClick = onConfirm,
            enabled = isConfirmEnabled
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if(displayIcon) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null
                    )

                    Spacer(Modifier.width(SpacingXS))
                }

                Text(
                    text = stringResource(primaryActionText),
                    style = titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}