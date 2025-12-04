package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.search.sheets.filters.SearchFiltersButton
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchHeader(
    modifier: Modifier = Modifier,
    activeFiltersCount: Int = 0,
    headline: String,
    subHeadline: String,
    onClick: () -> Unit,
    onFilter: () -> Unit
) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = BasePadding)
                .shadow(
                    elevation = 2.dp,
                    shape = CircleShape,
                    clip = false
                )
                .clip(shape = CircleShape)
                .background(Background)
                .clickable { onClick() }
                .then(modifier)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.padding(SpacingS),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(SpacingM)
                            .size(35.dp),
                        painter = painterResource(R.drawable.ic_search_solid),
                        contentDescription = null
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = SpacingS),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = headline,
                        fontWeight = FontWeight.SemiBold,
                        style = titleMedium,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(SpacingXS))
                    Text(
                        text = subHeadline,
                        color = Color.Gray,
                        style = bodyLarge
                    )
                }

                SearchFiltersButton(
                    activeFiltersCount = activeFiltersCount,
                    onFilter = onFilter
                )
            }
        }

//        when(permissionStatus) {
//            LocationPermissionStatus.NOT_DETERMINED,
//            LocationPermissionStatus.DENIED_CAN_ASK_AGAIN -> {
//                SearchEnableLocationBanner(
//                    modifier = Modifier.statusBarsPadding(),
//                    onEnableClick = {
//                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//                    },
//                    onCancel = {}
//                )
//            }
//            else -> Unit
//        }
    }
}