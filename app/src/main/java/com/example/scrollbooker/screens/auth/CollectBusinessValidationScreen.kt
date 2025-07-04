package com.example.scrollbooker.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun CollectBusinessValidationScreen() {
    FormLayout(
        headLine = "Felicitari! Ai finalizat inregistrarea",
        subHeadLine = "Am primit toate detaliile despre business-ul tau. In scurt timp, echipa noastra va revizui aplicatia si te vom anunta imediat ce contul tau este aprobat",
        enableBack = false,
        enableBottomAction = false,
    ) {
        Spacer(Modifier.height(SpacingXXL))
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(100.dp),
            painter = painterResource(R.drawable.ic_checkmark_solid),
            contentDescription = null,
            tint = Primary
        )
    }
}