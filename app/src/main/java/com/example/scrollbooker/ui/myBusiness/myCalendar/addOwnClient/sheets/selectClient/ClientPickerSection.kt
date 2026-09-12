package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.selectClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.SearchBar
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.entity.booking.businessClient.domain.model.BusinessClient
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.labelLarge
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun ClientPickerSection(
    selectedClient: BusinessClient?,
    onAddNewClient: () -> Unit,
    onSelectClient: () -> Unit,
    onRemoveClient: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.client),
                style = titleLarge,
                fontWeight = FontWeight.ExtraBold
            )

            TextButton(onClick = onAddNewClient, enabled = selectedClient == null) {
                Text(
                    text = stringResource(R.string.addNewClient),
                    style = labelLarge,
                    color = if (selectedClient == null) Primary else Primary.copy(alpha = 0.4f),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (selectedClient != null) {
            SelectedClientRow(
                client = selectedClient,
                onRemove = onRemoveClient
            )
        } else {
            Box {
                SearchBar(
                    value = "",
                    onValueChange = {},
                    placeholder = stringResource(R.string.searchClientByNameOrPhone),
                    shape = ShapeDefaults.ExtraLarge
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onSelectClient
                        )
                )
            }

            Spacer(Modifier.height(SpacingM))

            Text(
                modifier = Modifier.padding(bottom = BasePadding),
                text = stringResource(R.string.searchClientHint),
                style = bodyMedium
            )
        }
    }
}
