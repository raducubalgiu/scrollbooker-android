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
        text = when(type) {
            AuthTypeEnum.LOGIN -> stringResource(R.string.login)
            AuthTypeEnum.REGISTER -> stringResource(R.string.register)
            AuthTypeEnum.REGISTER_BUSINESS -> "Inregistrare business"
        }
    )

    Text(
        text = when(type) {
            AuthTypeEnum.LOGIN -> stringResource(R.string.loginMessage)
            AuthTypeEnum.REGISTER -> stringResource(R.string.registerMessage)
            AuthTypeEnum.REGISTER_BUSINESS -> stringResource(R.string.registerBusinessDescription)
        }
    )

    Spacer(Modifier.height(BasePadding))
}