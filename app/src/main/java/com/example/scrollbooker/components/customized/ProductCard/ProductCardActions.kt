package com.example.scrollbooker.components.customized.ProductCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Error

@Composable
fun ProductCardActions(
    isLoadingDelete: Boolean,
    onNavigateToEdit: () -> Unit,
    onDeleteProduct: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MainButtonOutlined(
            modifier = Modifier
                .weight(0.5f)
                .clip(shape = ShapeDefaults.ExtraLarge),
            title = stringResource(R.string.edit),
            onClick = onNavigateToEdit,
            icon = painterResource(R.drawable.ic_edit_outline),
            iconColor = Color.Gray
        )

        Spacer(Modifier.width(SpacingS))

        MainButtonOutlined(
            modifier = Modifier
                .weight(0.5f)
                .clip(shape = ShapeDefaults.ExtraLarge),
            title = stringResource(R.string.delete),
            isLoading = isLoadingDelete,
            isEnabled = !isLoadingDelete,
            onClick = onDeleteProduct,
            icon = painterResource(R.drawable.ic_delete_outline),
            iconColor = Error
        )
    }
}