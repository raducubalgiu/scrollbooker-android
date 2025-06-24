package com.example.scrollbooker.components.customized.post.comments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXS
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun CommentFooter(modifier: Modifier = Modifier) {
    val emoticons = listOf("\uD83D\uDC4C", "\uD83D\uDE01", "\uD83D\uDE07", "\uD83E\uDD23", "\uD83D\uDE0D", "\uD83E\uDD70")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        emoticons.forEach { emoji ->
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {}
            ) {
                Text(
                    modifier = Modifier.padding(vertical = SpacingM),
                    text = emoji,
                    style = TextStyle(
                        fontSize = 24.sp
                    )
                )
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(
            url = "",
            size = AvatarSizeXS
        )
        Spacer(Modifier.width(SpacingS))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = ShapeDefaults.ExtraLarge),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = ShapeDefaults.Large,
                value = "",
                onValueChange = {},
                placeholder = {
                    Text(
                        text = "Adauga un comentariu..",
                        style = bodyMedium,
                        color = Divider
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SurfaceBG,
                    unfocusedContainerColor = SurfaceBG,
                    cursorColor = Primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Primary,
                    unfocusedLabelColor = OnSurfaceBG.copy(alpha = 0.7f),
                    focusedTextColor = OnSurfaceBG,
                    unfocusedTextColor = OnSurfaceBG,
                    disabledContainerColor = SurfaceBG,
                    disabledTextColor = Divider,
                    disabledIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AlternateEmail,
                            contentDescription = "Mention Icon",
                            tint = OnBackground
                        )
                    }
                }
            )
        }
    }
}