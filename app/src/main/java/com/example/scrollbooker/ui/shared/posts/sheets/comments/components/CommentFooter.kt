package com.example.scrollbooker.ui.shared.posts.sheets.comments.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.customized.TextFieldComment
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.social.comment.domain.model.CreateComment
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun CommentFooter(onCreateComment: (CreateComment) -> Unit) {
    val emoticons = listOf(
        "\uD83D\uDC4C", "\uD83D\uDE01", "\uD83D\uDE07", "\uD83E\uDD23", "\uD83D\uDE0D", "\uD83E\uDD70"
    )
    var text by remember { mutableStateOf("") }
    var parentId by remember { mutableStateOf<Int?>(null) }

    Column {
        HorizontalDivider(color = Divider, thickness = 0.5.dp)

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = BasePadding,
                vertical = SpacingXS
            ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(emoticons) { emoji ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { text = text + emoji }
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

        TextFieldComment(
            avatar = "",
            value = text,
            isEnabled = text.isNotEmpty(),
            onValueChange = { text = it },
            onSubmit = {
                onCreateComment(CreateComment(text, parentId))
                text = ""
            }
        )
    }
}