package com.example.scrollbooker.feature.auth.presentation.components.collectClientDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.ScreenIndicator
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingL
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.headlineLarge
import com.example.scrollbooker.ui.theme.titleSmall

@Composable
fun CollectClientDetails(
    headLine: String,
    subHeadLine: String,
    screenSize: Int,
    selectedScreen: Int,
    onOmit: () -> Unit,
    onNext: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .safeDrawingPadding()
        .padding(
            top = 50.dp,
            bottom = BasePadding,
            start = BasePadding,
            end = BasePadding
        ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                style = headlineLarge,
                color = OnBackground,
                fontWeight = FontWeight.ExtraBold,
                text = headLine
            )
            Text(
                style = titleSmall,
                fontWeight = FontWeight.Normal,
                color = OnSurfaceBG.copy(alpha = 0.7f),
                text = subHeadLine,
            )
            Spacer(Modifier.height(SpacingL))
            content()
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScreenIndicator(screenSize = screenSize, selectedScreen = selectedScreen)
            Row {
                TextButton(onClick = onOmit) {
                    Text(text = stringResource(R.string.omit))
                }
                Button(onClick = onNext) {
                    Text(text = stringResource(R.string.nextStep))
                }
            }
        }
    }
}