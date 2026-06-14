package com.example.scrollbooker.components.customized.post.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.social.post.domain.model.PostUser
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun PostOverlayUser(
    user: PostUser,
    isVideoReview: Boolean,
    onNavigateToUser: (userId: Int, username: String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    when {
        isVideoReview -> {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.1f),
                    contentColor = OnBackground
                ),
                contentPadding = PaddingValues(
                    vertical = 10.dp,
                    horizontal = BasePadding
                )
            ) {
                Text(
                    text = "Recenzie video",
                    style = bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(vertical = SpacingS)
            .clickable(
                onClick = { onNavigateToUser(user.id, user.username) },
                interactionSource = interactionSource,
                indication = null
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user.fullName,
                style = bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontSize = 18.sp
            )
        }

        Spacer(Modifier.height(SpacingS))

        when {
            isVideoReview -> {
                SecondaryText(
                    text = "${stringResource(R.string.hasTestedTheService)} Tuns",
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Italic
                )
            }
            else -> SecondaryText(user.profession)
        }

        Spacer(Modifier.height(SpacingM))
    }
}

@Composable
private fun SecondaryText(
    text: String,
    color: Color = Primary.copy(0.85f),
    fontWeight: FontWeight = FontWeight.SemiBold,
    fontStyle: FontStyle = FontStyle.Normal
) {
    Text(
        text = text,
        style = TextStyle(
            shadow = Shadow(
                color = Color.Black.copy(alpha = 0.6f),
                offset = Offset(1f, 1f),
                blurRadius = 3f
            ),
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
        ),
        fontWeight = fontWeight,
        color = color,
        fontStyle = fontStyle
    )
}