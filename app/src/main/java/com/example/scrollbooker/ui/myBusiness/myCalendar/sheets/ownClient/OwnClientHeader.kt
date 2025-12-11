package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun OwnClientHeader(
    title: String,
    subTitle: String,
    onClose: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CustomIconButton(
            imageVector = Icons.Default.Close,
            tint = Color.Transparent,
            onClick = {}
        )

        Column(modifier = Modifier.padding(BasePadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = titleMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(SpacingS))

            Text(
                text = subTitle,
                style = titleMedium,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        }

        CustomIconButton(
            imageVector = Icons.Default.Close,
            onClick = onClose
        )
    }
}