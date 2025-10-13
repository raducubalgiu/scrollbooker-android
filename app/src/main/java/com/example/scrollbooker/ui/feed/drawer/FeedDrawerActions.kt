package com.example.scrollbooker.ui.feed.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun FeedDrawerActions(
    isResetVisible: Boolean,
    onReset: () -> Unit,
    onFilter: () -> Unit
) {
    Column {
        HorizontalDivider(
            color = Color(0xFF3A3A3A),
            thickness = 0.55.dp
        )
        Spacer(Modifier.height(BasePadding))

        AnimatedVisibility(
            visible = isResetVisible,
            enter = slideInVertically(initialOffsetY = { -20 }) + fadeIn(animationSpec = tween(250)),
            exit = slideOutVertically(targetOffsetY = { -20 }) + fadeOut(animationSpec = tween(250))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = BasePadding),
                contentAlignment = Alignment.Center
            ) {
                TextButton(onClick = onReset) {
                    Text(
                        text = stringResource(R.string.clearFilters),
                        color = Error,
                        style = bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        MainButton(
            onClick = onFilter,
            title = stringResource(R.string.filter),
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6F00),
                contentColor = Color(0xFFE0E0E0),
                disabledContainerColor = Color(0xFF1C1C1C),
                disabledContentColor = Color(0xFF3A3A3A),
            )
        )
    }
}