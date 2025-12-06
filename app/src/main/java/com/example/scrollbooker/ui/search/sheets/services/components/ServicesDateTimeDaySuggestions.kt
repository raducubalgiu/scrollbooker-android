package com.example.scrollbooker.ui.search.sheets.services.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
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
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleMedium
import org.threeten.bp.LocalDate
import toPrettyDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ServicesDateTimeDaySuggestions() {
    val today = remember { LocalDate.now() }
    val tomorrow = remember { today.plusDays(1) }

    Row(
        modifier = Modifier
            .padding(
                horizontal = BasePadding,
                vertical = SpacingXXL
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DaySuggestion(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.today),
            description = today.toPrettyDate()
        )

        Spacer(Modifier.width(BasePadding))

        DaySuggestion(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.tommorow),
            description = tomorrow.toPrettyDate()
        )
    }
}

@Composable
private fun DaySuggestion(
    modifier: Modifier = Modifier,
    title: String,
    description: String
) {
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Divider,
                shape = ShapeDefaults.Medium
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

            Spacer(Modifier.height(BasePadding))

            Text(
                text = description,
                color = Color.Gray
            )
        }
    }
}