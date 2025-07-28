package com.example.scrollbooker.ui.feed.components.search
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun FeedSearchKeyword(
    keyword: String,
    icon: Int = R.drawable.ic_search,
    onClick: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(
            vertical = SpacingM,
            horizontal = BasePadding
        )
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = { onClick(keyword) }
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Color.Gray
            )

            Spacer(Modifier.width(BasePadding))
            Text(
                text = keyword,
                style = bodyLarge
            )
        }
        Box(Modifier.padding(horizontal = SpacingM)) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.ArrowOutward,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}