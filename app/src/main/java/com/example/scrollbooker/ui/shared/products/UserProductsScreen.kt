package com.example.scrollbooker.ui.shared.products
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.navigation.navigators.NavigateCalendarParam
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun UserProductsScreen(
    userId: Int,
    onBack: () -> Unit,
    onNavigateToCalendar: (NavigateCalendarParam) -> Unit
) {
    Layout(
        modifier = Modifier.safeDrawingPadding(),
        onBack = onBack,
        enablePaddingH = false,
        headerTitle = stringResource(R.string.services)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AvatarWithRating(
                url = "",
                rating = 4.5f,
                size = 100.dp,
                onClick = {}
            )

            Spacer(Modifier.height(BasePadding))

            Text(
                text = "Radu Ion",
                style = titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(BasePadding))

        HorizontalDivider(
            color = Divider,
            thickness = 0.55.dp
        )
        UserProductsServiceTabs(
            userId = userId,
            onNavigateToCalendar = onNavigateToCalendar,
            paddingTop = 0.dp
        )
    }
}