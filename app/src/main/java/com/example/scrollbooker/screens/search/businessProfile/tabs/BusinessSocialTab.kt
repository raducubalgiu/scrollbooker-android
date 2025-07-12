package com.example.scrollbooker.screens.search.businessProfile.tabs
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.headlineMedium

@Composable
fun BusinessSocialTab() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = BasePadding)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = stringResource(R.string.social),
            style = headlineMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}