package com.example.scrollbooker.ui.feed.search
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.inputs.SearchBarSmall
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM

@Composable
fun FeedSearchHeader(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = false,
    onSearch: () -> Unit,
    onClearInput: () -> Unit,
    onClick: (() -> Unit)? = null,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = BasePadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.clickable(
            onClick = onBack,
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        )) {
            Box(
                modifier = Modifier.padding(SpacingM),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null
                )
            }
        }
        SearchBarSmall(
            value = value,
            onValueChange = onValueChange,
            onSearch = onSearch,
            readOnly = readOnly,
            onClearInput = onClearInput,
            onClick = onClick,
            modifier = Modifier
                .weight(1f)
                .then(modifier)
        )
    }
}