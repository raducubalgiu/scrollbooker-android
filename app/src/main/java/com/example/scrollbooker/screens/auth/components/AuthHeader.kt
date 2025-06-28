package com.example.scrollbooker.screens.auth.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.core.nav.host.AuthTypeEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.headlineLarge

@Composable
fun AuthHeader(
    type: AuthTypeEnum
) {
    Text(
        style = headlineLarge,
        color = OnBackground,
        fontWeight = FontWeight.ExtraBold,
        text = if(type == AuthTypeEnum.LOGIN) stringResource(R.string.login)
        else stringResource(R.string.register)
    )
    Text(
        text =
            if(type == AuthTypeEnum.LOGIN) stringResource(R.string.loginMessage)
            else stringResource(R.string.registerMessage)
    )

    Spacer(Modifier.height(BasePadding))
}