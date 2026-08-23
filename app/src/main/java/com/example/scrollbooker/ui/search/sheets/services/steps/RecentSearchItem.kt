package com.example.scrollbooker.ui.search.sheets.services.steps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.search.domain.model.RecentSearch
import com.example.scrollbooker.entity.search.domain.model.displayLabel
import com.example.scrollbooker.ui.theme.bodySmall

@Composable
fun RecentSearchItem(
    search: RecentSearch
) {
    Row(
        modifier = Modifier.padding(horizontal = BasePadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = null
        )

        Spacer(Modifier.width(BasePadding))

        Column {
            Text(
                text = search.serviceDomain.name,
                fontWeight = FontWeight.SemiBold
            )

            search.displayLabel()?.let {
                Text(
                    text = it,
                    color = Color.Gray,
                    style = bodySmall
                )
            }
        }
    }
}