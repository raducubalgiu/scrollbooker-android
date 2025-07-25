package com.example.scrollbooker.components.core.inputs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun SearchBarSmall(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClearInput: () -> Unit,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        modifier = Modifier
            .clip(shape = ShapeDefaults.Medium)
            .background(SurfaceBG)
            .then(modifier),
        value = value,
        singleLine = true,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 14.sp
        ),
        enabled = enabled,
        readOnly = readOnly,
        cursorBrush = SolidColor(Primary),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch() }
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = ShapeDefaults.Medium)
                    .height(44.dp)
                    .padding(horizontal = BasePadding)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onClick?.invoke() }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(20.dp),
                    tint = OnBackground
                )
                Spacer(Modifier.width(8.dp))

                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if(value.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                    innerTextField()
                }
                AnimatedVisibility(
                    visible = value.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Icon(
                        modifier = Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = onClearInput
                            ),
                        painter = painterResource(R.drawable.ic_close_circle_solid),
                        tint = Color.Gray.copy(alpha = 0.7f),
                        contentDescription = null
                    )
                }
            }
        }
    )

    if(!readOnly) {
        TextButton(
            enabled = value.isNotEmpty(),
            onClick = { onSearch() }
        ) {
            Text(
                text = stringResource(R.string.search),
                style = bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}