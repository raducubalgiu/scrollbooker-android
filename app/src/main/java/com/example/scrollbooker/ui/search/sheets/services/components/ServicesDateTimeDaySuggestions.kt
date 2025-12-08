package com.example.scrollbooker.ui.search.sheets.services.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.titleMedium
import org.threeten.bp.LocalDate
import toPrettyDate

@Composable
fun ServicesDateTimeDaySuggestions(
    today: LocalDate,
    tomorrow: LocalDate,
    isTodaySelected: Boolean,
    isTomorrowSelected: Boolean,
    onTodayClick: () -> Unit,
    onTomorrowClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(
                top = SpacingXXL,
                start = BasePadding,
                end = BasePadding,
                bottom = BasePadding,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DaySuggestion(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.today),
            description = today.toPrettyDate(),
            isSelected = isTodaySelected,
            onClick = onTodayClick
        )

        Spacer(Modifier.width(BasePadding))

        DaySuggestion(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.tommorow),
            description = tomorrow.toPrettyDate(),
            isSelected = isTomorrowSelected,
            onClick = onTomorrowClick
        )
    }
}

@Composable
private fun DaySuggestion(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .border(
                width = if(isSelected) 2.dp else 1.dp,
                color = if(isSelected) Primary else Divider,
                shape = ShapeDefaults.Medium
            )
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Column(modifier = Modifier.padding(BasePadding)) {
            Text(
                text = title,
                style = titleMedium,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(SpacingS))

            Text(
                text = description,
                color = Color.Gray
            )
        }
    }
}