package com.example.scrollbooker.screens.feed.tabs
import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.example.scrollbooker.components.Video
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleLarge
import com.example.scrollbooker.ui.theme.titleMedium

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(UnstableApi::class)
@Composable
fun BookNowTab(shouldVideoPlay: Boolean) {
    val state = rememberPagerState { 5 }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val flingBehavior = PagerDefaults.flingBehavior(
        state = state,
        snapPositionalThreshold = 0.15f,
        snapAnimationSpec = tween(durationMillis = 200)
    )

    Box(Modifier.fillMaxSize()) {
//        VerticalPager(
//            state = state,
//            modifier = Modifier
//                .height(screenHeight - 100.dp),
//            beyondViewportPageCount = 1,
//            flingBehavior = flingBehavior
//        ) { page ->
//            Box(Modifier.fillMaxSize()) {
//                Video(
//                    shouldVideoPlay=shouldVideoPlay
//                )
//                Box(modifier = Modifier
//                    .fillMaxSize()
//                    .padding(
//                        vertical = SpacingS,
//                        horizontal = BasePadding
//                    )
//                ) {
//                    Column(modifier = Modifier.fillMaxSize(),
//                        verticalArrangement = Arrangement.Bottom
//                    ) {
//                        Row(
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            verticalAlignment = Alignment.Bottom
//                        ) {
//                            Column(modifier = Modifier.weight(1f)) {
//                                Text("Left")
//                            }
//                            Column(modifier = Modifier) {
//                                Column(
//                                    horizontalAlignment = Alignment.CenterHorizontally,
//                                ) {
//
//                                    Spacer(Modifier.height(SpacingXS))
//                                    Icon(
//                                        imageVector = Icons.Default.Favorite,
//                                        contentDescription = null,
//                                        modifier = Modifier.size(35.dp),
//                                        tint = Error
//
//                                    )
//                                    Spacer(Modifier.height(SpacingXS))
//                                    Text(
//                                        text = "1,730",
//                                        style = titleMedium
//                                    )
//                                }
//                                Spacer(Modifier.height(BasePadding))
//                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                                    Icon(
//                                        imageVector = Icons.Default.ChatBubbleOutline,
//                                        contentDescription = null,
//                                        modifier = Modifier.size(35.dp)
//                                        )
//                                    Spacer(Modifier.height(SpacingXS))
//                                    Text(
//                                        text = "0",
//                                        style = titleMedium
//                                    )
//                                }
//                                Spacer(Modifier.height(BasePadding))
//                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                                    Icon(
//                                        imageVector = Icons.Default.BookmarkBorder,
//                                        contentDescription = null,
//                                        modifier = Modifier.size(35.dp)
//                                    )
//                                    Spacer(Modifier.height(SpacingXS))
//                                    Text(
//                                        text = "200",
//                                        style = titleMedium
//                                    )
//                                }
//                                Spacer(Modifier.height(BasePadding))
//                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                                    Icon(
//                                        imageVector = Icons.Default.Share,
//                                        contentDescription = null,
//                                        modifier = Modifier.size(35.dp)
//                                    )
//                                }
//                                Spacer(Modifier.height(BasePadding))
//                            }
//                        }
//
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ){
//                            Button(
//                                modifier = Modifier.weight(0.5f),
//                                onClick = {},
//                                shape = MaterialTheme.shapes.small,
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = Primary.copy(alpha = 0.9f),
//                                    contentColor = OnPrimary
//                                )
//                            ) {
//                                Text(
//                                    text = "Rezerva Instant",
//                                    style = titleMedium
//                                )
//                            }
//                            Spacer(Modifier.width(SpacingM))
//                            Button(
//                                modifier = Modifier.weight(0.5f),
//                                onClick = {},
//                                shape = MaterialTheme.shapes.small,
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = Color.White.copy(alpha = 0.1f),
//                                    contentColor = OnBackground
//                                )
//                            ) {
//                                Text(
//                                    text = "Liber: Luni 14:00",
//                                    style = titleMedium
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }
}