package com.example.scrollbooker.ui.shared.posts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun PostActionButtonSmall(
    onNavigateToCalendar: () -> Unit,
    onNavigateToProducts: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = BasePadding),
        onClick = onNavigateToProducts,
        shape = ShapeDefaults.Medium,
        colors = ButtonColors(
            containerColor = Primary,
            contentColor = OnPrimary,
            disabledContainerColor = Primary,
            disabledContentColor = OnPrimary
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_shopping_outline),
                contentDescription = null
            )
            Spacer(Modifier.width(BasePadding))
            Text(
                text = stringResource(R.string.book),
                style = bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = OnPrimary
            )
        }
    }
}