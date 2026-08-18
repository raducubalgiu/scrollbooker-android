package com.example.scrollbooker.ui.booking.specialists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlowUser
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectSpecialistsSheet(
    sheetState: SheetState,
    employees: List<BookingFlowUser>,
    selectedEmployee: BookingFlowUser?,
    onConfirmEmployee: (BookingFlowUser) -> Unit,
    onClose: () -> Unit
) {
    var selectedLocally by remember {
        mutableStateOf(selectedEmployee)
    }

    Sheet(
        modifier = Modifier.fillMaxWidth(),
        sheetState = sheetState,
        onClose = onClose,
    ) {
        SheetHeader(
            title = stringResource(R.string.employees),
            onClose = onClose
        )

        employees.forEach { specialist ->
            SpecialistItem(
                specialist = specialist,
                isSelected = selectedLocally?.id == specialist.id,
                onSelect = { selectedLocally = it }
            )

            if(specialist != employees.last()) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = BasePadding),
                    color = Divider,
                    thickness = 0.55.dp
                )
            }
        }

        Spacer(Modifier.height(SpacingXL))

        MainButton(
            modifier = Modifier.padding(horizontal = BasePadding),
            title = stringResource(R.string.add),
            onClick = { selectedLocally?.let { onConfirmEmployee(it) } },
            enabled = selectedLocally != null
        )
    }
}

@Composable
fun SpecialistItem(
    isSelected: Boolean,
    specialist: BookingFlowUser,
    onSelect: (BookingFlowUser) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(
                    onClick = { onSelect(specialist) },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            horizontalArrangement = Arrangement.spacedBy(SpacingM),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarWithRating(
                url = specialist.avatar ?: "",
                size = 55.dp,
                rating = specialist.ratingsAverage,
                onClick = { onSelect(specialist) }
            )

            Column(verticalArrangement =  Arrangement.spacedBy(SpacingXXS)) {
                Text(
                    text = specialist.fullName,
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )

                Text(
                    text = specialist.profession,
                    style = bodyMedium,
                    color = Color.Gray
                )
            }
        }

        RadioButton(
            selected = isSelected,
            onClick = { onSelect(specialist) }
        )
    }
}