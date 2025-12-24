package com.example.scrollbooker.ui.shared.posts.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
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
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = BasePadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier.weight(1f),
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

                Spacer(Modifier.width(SpacingM))

                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Background,
                        contentColor = OnBackground
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.size(17.dp),
                            painter = painterResource(R.drawable.ic_call_outline),
                            contentDescription = null,
                            tint = OnBackground
                        )

                        Spacer(Modifier.width(SpacingS))

                        Text(
                            text = "Sună",
                            style = bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = OnBackground
                        )
                    }
                }
            }
        }
    }
}