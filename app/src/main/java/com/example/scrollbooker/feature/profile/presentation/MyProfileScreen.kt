package com.example.scrollbooker.feature.profile.presentation
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
import com.example.scrollbooker.core.util.Dimens.SpacingL
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.feature.profile.presentation.components.CounterItem
import com.example.scrollbooker.feature.profile.presentation.components.ProfileHeader
import com.example.scrollbooker.feature.profile.presentation.components.ProfileTabs
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.labelLarge
import com.example.scrollbooker.ui.theme.labelMedium
import com.example.scrollbooker.ui.theme.titleMedium
import com.example.scrollbooker.ui.theme.titleSmall

@Composable
fun MyProfileScreen(
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
        ProfileHeader(
            username = user?.username.toString(),
            onOpenBottomSheet = { showBottomSheet = true }
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 70.dp,
                vertical = SpacingXXL),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CounterItem(
                counter = ratingsCount,
                label = stringResource(R.string.reviews),
                onNavigate = {
                    if(user != null) onNavigate("${MainRoute.UserSocial.route}/${0}/${user.id}/${user.username}")
                }
            )
            VerticalDivider()
            CounterItem(
                counter = followersCount,
                label = stringResource(R.string.followers),
                onNavigate = {
                    if(user != null) onNavigate("${MainRoute.UserSocial.route}/${1}/${user.id}/${user.username}")
                }
            )
            VerticalDivider()
            CounterItem(
                counter = followingsCount,
                label = stringResource(R.string.following),
                onNavigate = {
                    if(user != null) onNavigate("${MainRoute.UserSocial.route}/${2}/${user.id}/${user.username}")
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
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(1.dp, Divider, CircleShape)
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
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(10.dp))
                    Spacer(Modifier.height(SpacingXXS))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = viewModel.user?.profession ?: "",
                            style = titleSmall,
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
                    Spacer(Modifier.height(SpacingS))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = OnSurfaceBG
                        )
                        Spacer(Modifier.width(7.dp))
                        Text(
                            text = "Inchide la 19:00",
                            style = bodyMedium,
                            color = OnSurfaceBG
                        )
                        Spacer(Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowDown,
                            contentDescription = null,
                            tint = OnSurfaceBG
                        )
                    }
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().padding(vertical = SpacingXXL, horizontal = BasePadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier.weight(5f),
                onClick = {},
                shape = RoundedCornerShape(size = 7.5.dp),
                contentPadding = PaddingValues(
                    vertical = 15.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = OnPrimary
                )
            ) {
                Text(text = stringResource(R.string.follow))
            }
            Spacer(Modifier.width(SpacingS))
            Button(
                modifier = Modifier.weight(5f),
                onClick = {},
                shape = RoundedCornerShape(size = 7.5.dp),
                contentPadding = PaddingValues(
                    vertical = 16.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SurfaceBG,
                    contentColor = OnSurfaceBG
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.clip(CircleShape).background(Color.Green).height(10.dp).width(10.dp)
                    )
                    Spacer(Modifier.width(SpacingS))
                    Text("5 locuri libere")
                }
            }
            Spacer(Modifier.width(SpacingS))
            Button(
                modifier = Modifier.weight(1.5f),
                onClick = {},
                shape = RoundedCornerShape(size = 7.5.dp),
                contentPadding = PaddingValues(
                    vertical = 14.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SurfaceBG,
                    contentColor = OnSurfaceBG
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }
//        MainButton(modifier = Modifier.padding(BasePadding),
//            onClick = {},
//            title = stringResource(R.string.follow)
//        )
        ProfileTabs()
    }
}