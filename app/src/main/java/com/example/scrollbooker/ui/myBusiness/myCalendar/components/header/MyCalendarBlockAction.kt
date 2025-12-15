package com.example.scrollbooker.ui.myBusiness.myCalendar.components.header

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun MyCalendarBlockAction(
    isEnabled: Boolean,
    isBlocking: Boolean,
    onCancel: () -> Unit,
    onBlockConfirm: () -> Unit
) {
    AnimatedContent(
        targetState = isBlocking,
        transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
        label = "HeaderTransition"
    ) { target ->
        if(target) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = BasePadding)
                .background(Background)
            ) {
                HorizontalDivider(color = Divider, thickness = 0.55.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(BasePadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MainButton(
                        modifier = Modifier.weight(0.5f),
                        fullWidth = false,
                        onClick = onCancel,
                        title = stringResource(R.string.cancel),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceBG,
                            contentColor = OnSurfaceBG
                        )
                    )
                    Spacer(Modifier.width(SpacingS))
                    MainButton(
                        modifier = Modifier.weight(0.5f),
                        fullWidth = false,
                        onClick = onBlockConfirm,
                        enabled = isEnabled,
                        title = stringResource(R.string.block),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Error.copy(alpha = 0.2f),
                            contentColor = Error
                        )
                    )
                }
            }
        }
    }
}