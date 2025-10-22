package com.example.scrollbooker.ui.shared.posts.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun PostActionButtonSmall(
    show: Boolean,
    title: String,
    onClick: () -> Unit
) {
    AnimatedContent(
        targetState = show,
        transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
        label = "HeaderTransition"
    ) { target ->
        if(target) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = BasePadding),
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = OnPrimary
                )
            ) {
                Text(
                    text = title,
                    style = bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = OnPrimary
                )
            }
        }
    }
}