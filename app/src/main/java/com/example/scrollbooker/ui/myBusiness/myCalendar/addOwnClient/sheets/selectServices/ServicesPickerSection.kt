package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.selectServices
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.placeholderActionBox.PlaceholderActionBox
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.booking.SelectedBookingItem
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.labelLarge
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun ServicesPickerSection(
    linkedItems: List<SelectedBookingItem>,
    onOpenSheet: () -> Unit,
    onRemoveItem: (SelectedBookingItem) -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SpacingM),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.services),
                style = titleLarge,
                fontWeight = FontWeight.ExtraBold
            )

            if (linkedItems.isNotEmpty()) {
                TextButton(onClick = onOpenSheet) {
                    Text(
                        text = stringResource(R.string.change),
                        style = labelLarge,
                        color = Primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (linkedItems.isEmpty()) {
            PlaceholderActionBox(
                modifier = Modifier.padding(bottom = BasePadding),
                description = stringResource(R.string.selectServicesDescription),
                icon = Icons.Default.Add,
                onClick = onOpenSheet
            )
        } else {
            linkedItems.forEachIndexed { index, item ->
                LinkedServiceItemRow(
                    modifier = Modifier.padding(vertical = BasePadding),
                    item = item,
                    onRemove = onRemoveItem
                )

                if (index < linkedItems.size - 1) {
                    HorizontalDivider(thickness = 0.55.dp, color = Divider)
                }
            }
        }
    }
}
