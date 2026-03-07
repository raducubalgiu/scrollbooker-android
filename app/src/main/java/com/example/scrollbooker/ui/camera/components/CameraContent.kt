package com.example.scrollbooker.ui.camera.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.headlineMedium

@Composable
fun CameraContent(
    onBack: () -> Unit,
    showSettingsCta: Boolean,
    openAppSettings: () -> Unit
) {
    Column {
        CustomIconButton(
            imageVector = Icons.Default.Close,
            boxSize = 60.dp,
            iconSize = 30.dp,
            tint = Color.White,
            onClick = onBack
        )

        Column(
            modifier = Modifier
                .padding(top = 100.dp)
                .padding(horizontal = SpacingXL),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(50.dp),
                painter = painterResource(R.drawable.ic_video_slash_outline),
                contentDescription = null,
                tint = Color.White
            )

            Spacer(Modifier.height(BasePadding))

            Text(
                text = stringResource(R.string.cameraIsDeactivated),
                color = Color.White,
                style = headlineMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(SpacingS))

            Text(
                text = stringResource(R.string.forTheMomentCameraIsDeactivatedChooseFromGallery),
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            if (showSettingsCta) {
                Spacer(Modifier.height(BasePadding))

                Button(
                    onClick = { openAppSettings() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.15f)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.openSettings),
                        color = Color.White
                    )
                }
            }
        }
    }
}