package com.example.scrollbooker.ui.feed.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun FeedDrawerHeader() {
    Image(
        painter = painterResource(R.drawable.ic_logo_drawer),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        alignment = Alignment.CenterStart,
        modifier = Modifier
            .padding(top = SpacingXL)
            .height(70.dp)
    )

    Spacer(Modifier.height(SpacingXS))

    Text(
        modifier = Modifier.padding(end = SpacingS),
        style = titleLarge,
        fontSize = 18.sp,
        color = Color(0xFFAAAAAA),
        text = stringResource(R.string.chooseWhatDoYouWantToSeeInFeed)
    )

    Spacer(Modifier.height(SpacingXL))
}