package com.example.scrollbooker.ui.search.sheets.services.components
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.search.sheets.SearchSheetActions
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun SearchServicesSheetFooter(
    isClearEnabled: Boolean,
    isConfirmEnabled: Boolean,
    onConfirm: () -> Unit,
    onClear: () -> Unit,
    onOpenDate: () -> Unit,
    summary: String?,
    isActive: Boolean
) {
    Column {
        HorizontalDivider(
            modifier = Modifier.padding(bottom = BasePadding),
            color = Divider,
            thickness = 0.55.dp
        )

        DateTimeButton(
            title = if(isActive) summary.toString() else stringResource(R.string.dateAndHour),
            icon = painterResource(R.drawable.ic_clock_outline),
            isActive = isActive,
            onClick = onOpenDate
        )

        Spacer(Modifier.width(BasePadding))

        SearchSheetActions(
            onClear = onClear,
            onConfirm = onConfirm,
            isConfirmEnabled = isConfirmEnabled,
            isClearEnabled = isClearEnabled
        )
    }
}

@Composable
private fun DateTimeButton(
    title: String,
    icon: Painter? = null,
    isActive: Boolean = false,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.padding(horizontal = BasePadding),
        onClick = onClick,
        shape = ShapeDefaults.Medium,
        tonalElevation = if (isActive) 1.dp else 0.dp,
        color = if (isActive) SurfaceBG.copy(alpha = 0.8f) else SurfaceBG
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = SpacingXL,
                    vertical = BasePadding
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Icon(
                    painter = painterResource(R.drawable.ic_clock_outline),
                    contentDescription = null,
                    tint = Color.Gray
                )
                Spacer(Modifier.width(8.dp))
            }

            Spacer(Modifier.width(BasePadding))

            Text(
                text = title,
                style = titleMedium,
                color = OnBackground
            )

            Spacer(Modifier.width(SpacingXL))

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}