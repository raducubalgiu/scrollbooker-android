package com.example.scrollbooker.ui.auth.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.isKeyboardOpen
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun AuthFooterBusinessNavigation(
    onNavigateToRegisterBusiness: () -> Unit
) {
    val isKeyboardOpen = isKeyboardOpen()

    AnimatedVisibility(
        visible = !isKeyboardOpen,
        enter = slideInVertically { fullHeight -> fullHeight },
        exit = ExitTransition.None
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(color = Divider, thickness = 0.5.dp)

            Spacer(Modifier.height(BasePadding))

            Text(
                text = stringResource(R.string.doYouHaveABusinessWhichReceivesAppointments),
                style = titleMedium,
                fontWeight = FontWeight.Normal
            )

            Spacer(Modifier.height(BasePadding))

            MainButtonOutlined(
                title = stringResource(R.string.registerNow),
                onClick = onNavigateToRegisterBusiness
            )
        }
    }
}