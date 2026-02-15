package com.example.scrollbooker.components.customized.Picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.headlineSmall

@Composable
fun PickerHeader(
    title: String,
    isBackEnabled: Boolean,
    isNextEnabled: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = BasePadding,
                end = BasePadding,
                bottom = BasePadding
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                enabled = isBackEnabled,
                onClick = onBack
            ) {
                Icon(
                    modifier = Modifier.size(15.dp),
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                )
            }

            Spacer(Modifier.width(SpacingXS))

            IconButton(
                enabled = isNextEnabled,
                onClick = onNext
            ) {
                Icon(
                    modifier = Modifier.size(15.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                )
            }
        }
    }
}