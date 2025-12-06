package com.example.scrollbooker.ui.search.sheets.services.steps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.generateTimeSlots
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ServicesTimeSection(
    selection: DateTimeSelection,
    onCancel: () -> Unit,
    onPresetSelection: (TimePreset) -> Unit
) {
    Column {
        Text(
            text = "Interval Orar",
            style = titleMedium,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(
                top = BasePadding,
                bottom = 8.dp,
                start = BasePadding,
                end = BasePadding
            )
        )

        Spacer(Modifier.height(BasePadding))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BasePadding)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(SpacingM)
        ) {
            Column(
                modifier = Modifier
                    .heightIn(min = 65.dp)
                    .widthIn(min = 120.dp)
                    .border(
                        width = 2.dp,
                        color = Primary,
                        shape = ShapeDefaults.Medium
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    text = "Oricand",
                )
            }

            Column(
                modifier = Modifier
                    .heightIn(min = 65.dp)
                    .widthIn(min = 130.dp)
                    .border(
                        width = 1.dp,
                        color = Divider,
                        shape = ShapeDefaults.Medium
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    text = "La pranz"
                )
                Spacer(Modifier.height(SpacingXS))
                Text(
                    text = "12:00 - 18:00",
                    color = Color.Gray
                )
            }

            Column(
                modifier = Modifier
                    .heightIn(min = 65.dp)
                    .widthIn(min = 130.dp)
                    .border(
                        width = 1.dp,
                        color = Divider,
                        shape = ShapeDefaults.Medium
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    text = "Seara"
                )
                Spacer(Modifier.height(SpacingXS))
                Text(
                    text = "18:00 - 22:00",
                    color = Color.Gray
                )
            }

            Column(
                modifier = Modifier
                    .heightIn(min = 65.dp)
                    .widthIn(min = 130.dp)
                    .border(
                        width = 1.dp,
                        color = Divider,
                        shape = ShapeDefaults.Medium
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    text = "Dimineata"
                )
                Spacer(Modifier.height(SpacingXS))
                Text(
                    text = "09:00 - 12:00",
                    color = Color.Gray
                )
            }

            Column(
                modifier = Modifier
                    .heightIn(min = 65.dp)
                    .widthIn(min = 130.dp)
                    .border(
                        width = 1.dp,
                        color = Divider,
                        shape = ShapeDefaults.Medium
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    text = "Custom"
                )
            }
        }

        val slots = remember { generateTimeSlots() }

        AnimatedVisibility(visible = selection.preset == TimePreset.CUSTOM) {
            Spacer(Modifier.height(16.dp))
            // Inputs from - to Here
        }

        Spacer(Modifier.height(BasePadding))

        ServiceDateTimeFooter(
            onCancel = {},
            onConfirm = {}
        )
    }
}