package com.example.scrollbooker.ui.feed.drawer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.headlineLarge
import com.example.scrollbooker.ui.theme.headlineMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun FeedDrawerHeader() {
    Text(
        modifier = Modifier.padding(
            top = SpacingXL,
            bottom = SpacingM
        ),
        style = headlineLarge,
        color = Color(0xFFE0E0E0),
        fontWeight = FontWeight.SemiBold,
        text = "ScrollBooker"
    )

    Text(
        modifier = Modifier.padding(end = SpacingS),
        style = headlineMedium,
        fontSize = 26.sp,
        color = Color(0xFFE0E0E0),
        fontWeight = FontWeight.SemiBold,
        text = stringResource(R.string.chooseWhatDoYouWantToSeeInFeed)
    )

    Spacer(Modifier.height(40.dp))

    Text(
        modifier = Modifier.padding(bottom = BasePadding),
        style = titleMedium,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White.copy(0.8f),
        text = stringResource(R.string.categories)
    )
}