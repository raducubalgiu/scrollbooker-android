package com.example.scrollbooker.components.customized.productCard.detailSheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.headlineMedium

@Composable
fun ProductDetailInfo(
    name: String,
    description: String?
) {
    Column(Modifier.padding(horizontal = BasePadding)) {
        Text(
            modifier = Modifier.padding(bottom = SpacingXXS),
            text = name,
            style = headlineMedium,
            fontWeight = FontWeight.SemiBold
        )

        if(description.isNullOrBlank()) {
            Text(
                text = stringResource(R.string.serviceWithoutDescription),
                style = bodyMedium,
                color = Color.Gray
            )
        } else {
            Text(
                text = description,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}