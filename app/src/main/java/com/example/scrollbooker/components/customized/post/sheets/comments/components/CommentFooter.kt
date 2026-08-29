package com.example.scrollbooker.components.customized.post.sheets.comments.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.TextFieldComment
import com.example.scrollbooker.components.customized.post.sheets.comments.ReplyTarget
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun CommentFooter(
    replyTarget: ReplyTarget?,
    onCancelReply: () -> Unit,
    onCreateComment: (text: String) -> Unit
) {
    val emoticons = listOf(
        "👌", "😁", "😇", "🤣", "😍", "🥰"
    )
    var fieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(replyTarget?.parentId, replyTarget?.replyToCommentId) {
        if (replyTarget != null) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Column {
        HorizontalDivider(color = Divider, thickness = 0.5.dp)

        if (replyTarget != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BasePadding, vertical = SpacingXS),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .clip(ShapeDefaults.ExtraLarge)
                        .background(SurfaceBG)
                        .padding(horizontal = SpacingM, vertical = SpacingS),
                    text = buildAnnotatedString {
                        append(stringResource(R.string.replyingToPrefix))
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                color = Primary,
                            )
                        ) {
                            append(" ")
                            append("@${replyTarget.replyToUsername}")
                        }
                    },
                    style = bodyMedium,
                    color = OnBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.width(SpacingXS))

                IconButton(
                    modifier = Modifier.size(28.dp),
                    onClick = onCancelReply
                ) {
                    Icon(
                        modifier = Modifier.size(14.dp),
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = OnBackground
                    )
                }
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = BasePadding, vertical = SpacingXS),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(emoticons) { emoji ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            val newText = fieldValue.text + emoji
                            fieldValue = TextFieldValue(
                                text = newText,
                                selection = TextRange(newText.length)
                            )
                        }
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = SpacingM),
                        text = emoji,
                        style = TextStyle(fontSize = 24.sp)
                    )
                }
            }
        }

        TextFieldComment(
            avatar = "",
            value = fieldValue,
            isEnabled = fieldValue.text.isNotEmpty(),
            onValueChange = { fieldValue = it },
            onSubmit = {
                onCreateComment(fieldValue.text)
                fieldValue = TextFieldValue("")
            },
            focusRequester = focusRequester
        )
    }
}
