package com.example.scrollbooker.ui.appointments.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsFilterSheet(
    sheetState: SheetState,
    onChange: (AppointmentFilter) -> Unit,
    selectedOption: AppointmentFilter?,
    onCloseSheet: () -> Unit,
    onFilter: () -> Unit
) {
    val filters = listOf(
        AppointmentFilter(
            title = AppointmentFilterTitleEnum.ALL,
            asCustomer = null
        ),
        AppointmentFilter(
            title = AppointmentFilterTitleEnum.AS_EMPLOYEE,
            asCustomer = false
        ),
        AppointmentFilter(
            title = AppointmentFilterTitleEnum.AS_CLIENT,
            asCustomer = true
        ),
    )

    Sheet(
        sheetState = sheetState,
        onClose = onCloseSheet
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(BasePadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.filterAppointments),
                style = titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        LazyColumn {
            itemsIndexed(filters) { index, filter ->
                InputRadio(
                    selected = filter.title == selectedOption?.title,
                    headLine = filter.title.getLabel(),
                    onSelect = { onChange(filter) },
                )

                if(index < filters.size) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = SpacingXL),
                        color = Divider,
                        thickness = 0.55.dp
                    )
                }
            }


        }

        MainButton(
            modifier = Modifier.padding(
                vertical = BasePadding,
                horizontal = SpacingXXL
            ),
            title = stringResource(R.string.filter),
            onClick = onFilter
        )
    }
}