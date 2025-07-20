package com.example.scrollbooker.screens.feed.search
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun FeedSearchKeyword(
    keyword: String,
    icon: Int = R.drawable.ic_search,
    displayRightIcon: Boolean = true
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable {}
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(BasePadding),
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
            if(displayRightIcon) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_up_right_solid),
                    contentDescription = null
                )
            }
        }
    }
}