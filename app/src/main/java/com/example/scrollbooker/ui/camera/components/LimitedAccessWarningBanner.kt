package com.example.scrollbooker.ui.camera.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.labelSmall
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun LimitedAccessWarningBanner(
    onOpenSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = SurfaceBG,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(BasePadding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.someVideosMightNotAppear),
            style = titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = OnBackground
        )

        Text(
            text = stringResource(R.string.someVideosMightNotAppearLimitedAccess),
            style = bodySmall,
            color = Color.Gray
        )

        Button(
            onClick = onOpenSettings,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = OnPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.openSettings),
                style = labelSmall
            )
        }
    }
}