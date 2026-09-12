package com.example.scrollbooker.components.core.dropdown
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.OnBackground

@Composable
fun OwnIdentityChip(
    avatarUrl: String?,
    name: String?,
    compact: Boolean = false
) {
    val avatarSize = if (compact) 26.dp else 40.dp
    val horizontalPadding = if (compact) SpacingS else 16.dp
    val verticalPadding = if (compact) 6.dp else 8.dp

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(Color.Transparent)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(100.dp)
            )
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(if (compact) SpacingXS else 16.dp)
    ) {
        Avatar(url = avatarUrl ?: "", size = avatarSize)

        Text(
            text = name ?: "",
            color = OnBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
