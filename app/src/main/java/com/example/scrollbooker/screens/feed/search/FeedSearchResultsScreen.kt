package com.example.scrollbooker.screens.feed.search
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import kotlinx.coroutines.launch

@Composable
fun FeedSearchResultsScreen(
    viewModel: FeedSearchViewModel,
    onBack: () -> Unit
) {
    val tabs = listOf(
        "For You",
        "Users",
        "Last Minute",
        "Instant Booking",
        "Servicii",
        "Reviews"
    )

    val pagerState = rememberPagerState(initialPage = 0 ) { 6 }
    val selectedTabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    val value = ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
    )  {
        Row(
            modifier = Modifier.fillMaxWidth().padding(end = BasePadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.clickable(
                onClick = onBack,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )) {
                Box(
                    modifier = Modifier.padding(SpacingM),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = null
                    )
                }
            }
            BasicTextField(
                modifier = Modifier
                    .weight(1f)
                    .clip(shape = ShapeDefaults.Medium)
                    .background(SurfaceBG),
                value = "",
                singleLine = true,
                onValueChange = {  },
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 14.sp
                ),
                cursorBrush = SolidColor(Primary),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = ShapeDefaults.Medium)
                            .height(44.dp)
                            .padding(horizontal = BasePadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = "Search Icon",
                            modifier = Modifier.size(20.dp),
                            tint = OnBackground
                        )
                        Spacer(Modifier.width(8.dp))

                        Box(modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if(value.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.search),
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                            innerTextField()
                        }
                        AnimatedVisibility(
                            visible = value.isNotEmpty(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Icon(
                                modifier = Modifier.clickable {  },
                                painter = painterResource(R.drawable.ic_close_circle_solid),
                                tint = Color.Gray.copy(alpha = 0.7f),
                                contentDescription = null
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {

                    }
                )
            )
        }

        Spacer(Modifier.height(SpacingXS))

        ScrollableTabRow(
            containerColor = Background,
            contentColor = OnSurfaceBG,
            edgePadding = BasePadding,
            selectedTabIndex = pagerState.currentPage,
            indicator = {  tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(3.5.dp)
                        .padding(horizontal = 20.dp)
                        .background(
                            color = OnBackground,
                            shape = ShapeDefaults.Large
                        )
                )
            },
            divider = { HorizontalDivider(color = Divider, thickness = 0.55.dp) },
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedTabIndex == index

                Box(modifier = Modifier
                    .fillMaxWidth()
                    //.padding(vertical = 8.dp)
                    .clickable {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 10.dp,
                                horizontal = 14.dp
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            style = bodyLarge,
                            fontSize = 16.sp,
                            color = if (isSelected) OnSurfaceBG else Color.Gray,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 0,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when(page) {
                0 -> Box(Modifier.fillMaxSize()) {
                    Text("For You")
                }
                1 -> Box(Modifier.fillMaxSize()) {
                    Text("Users")
                }
                2 -> Box(Modifier.fillMaxSize()) {
                    Text("Servicii")
                }
                3 -> Box(Modifier.fillMaxSize()) {
                    Text("Last Minute")
                }
                4 -> Box(Modifier.fillMaxSize()) {
                    Text("Instant Booking")
                }
                5 -> Box(Modifier.fillMaxSize()) {
                    Text("Reviews")
                }
            }
        }
    }
}