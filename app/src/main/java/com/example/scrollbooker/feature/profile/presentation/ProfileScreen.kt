package com.example.scrollbooker.feature.profile.presentation
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.BottomSheet
import com.example.scrollbooker.components.MainButton
import com.example.scrollbooker.components.core.VerticalDivider
import com.example.scrollbooker.components.list.ItemList
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.feature.profile.components.CounterItem
import com.example.scrollbooker.feature.profile.presentation.components.ProfileHeader
import com.example.scrollbooker.feature.profile.presentation.components.ProfileTabs
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ProfileScreen(
    viewModel: ProfileSharedViewModel,
    onNavigate: (String) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val user = viewModel.user
    val ratingsCount = viewModel.user?.counters?.ratingsCount ?: 0
    val followersCount = viewModel.user?.counters?.followersCount ?: 0
    val followingsCount = viewModel.user?.counters?.followingsCount ?: 0

    BottomSheet(
        onDismiss = { showBottomSheet = false },
        showBottomSheet = showBottomSheet,
        showHeader = false,
        content = {
            ItemList(
                headLine = stringResource(id = R.string.calendar),
                leftIcon = painterResource(R.drawable.ic_calendar),
                displayRightIcon = false,
                onClick = {
                    showBottomSheet = false
                    onNavigate(MainRoute.Calendar.route)
                }
            )
            ItemList(
                headLine = stringResource(id = R.string.myBusiness),
                leftIcon = painterResource(R.drawable.ic_business),
                displayRightIcon = false,
                onClick = {
                    showBottomSheet = false
                    onNavigate(MainRoute.MyBusiness.route)
                }
            )
            ItemList(
                headLine = stringResource(id = R.string.settings),
                leftIcon = painterResource(R.drawable.ic_settings),
                displayRightIcon = false,
                onClick = {
                    showBottomSheet = false
                    onNavigate(MainRoute.Settings.route)
                }
            )
        }
    )

    Column(Modifier.fillMaxSize()) {
        ProfileHeader(onOpenBottomSheet = { showBottomSheet = true })
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 70.dp, vertical = SpacingXXL),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CounterItem(
                counter = ratingsCount,
                label = stringResource(R.string.reviews),
                onNavigate = {
                    if(user != null) onNavigate("${MainRoute.UserSocial.route}/${user.id}/${user.username}")
                }
            )
            VerticalDivider()
            CounterItem(
                counter = followersCount,
                label = stringResource(R.string.followers),
                onNavigate = {
                    if(user != null) onNavigate("${MainRoute.UserSocial.route}/${user.id}/${user.username}")
                }
            )
            VerticalDivider()
            CounterItem(
                counter = followingsCount,
                label = stringResource(R.string.following),
                onNavigate = {
                    if(user != null) onNavigate("${MainRoute.UserSocial.route}/${user.id}/${user.username}")
                }
            )
        }
        Spacer(Modifier.height(BasePadding))
        Column(modifier = Modifier
            .padding(horizontal = SpacingXL)
            .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Image(
                        painter = painterResource(R.drawable.ic_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(85.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(1.dp, Color.Gray, CircleShape)
                    )
                    Box(modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                        .border(3.dp, Color.White, CircleShape)
                    )
                }
                Spacer(Modifier.width(BasePadding))
                Column {
                    Text(
                        text = viewModel.user?.fullName ?: "",
                        style = titleMedium,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(10.dp))
                    Spacer(Modifier.height(SpacingS))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = viewModel.user?.profession ?: "",
                            style = bodyMedium,
                            modifier = Modifier.weight(1f, fill = false),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Primary
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = "4.5",
                            style = titleMedium,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnBackground
                        )
                    }
                    Spacer(Modifier.height(SpacingM))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(7.dp))
                        Text(
                            text = "Deschis acum",
                            style = bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(SpacingM))
        MainButton(
            modifier = Modifier.padding(BasePadding),
            onClick = {},
            title = stringResource(R.string.follow)
        )
        ProfileTabs()
    }
}