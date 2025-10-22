package com.example.scrollbooker.ui.feed.searchResults.tab.forYou

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.booking.business.domain.model.RecommendedBusiness
import com.example.scrollbooker.entity.booking.business.domain.model.RecommendedBusinessUser
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.example.scrollbooker.ui.feed.components.search.FeedSearchRecommendedBusiness
import com.example.scrollbooker.ui.social.components.UserSocialItem
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.delay

@Composable
fun FeedSearchForYouTab(modifier: Modifier = Modifier) {
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        delay(400)
        isLoading = false
    }

    if(isLoading) {
        LoadingScreen()
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier
                    .padding(horizontal = BasePadding)
                    .padding(top = BasePadding, bottom = SpacingXS),
                text = "Utilizatori",
                style = titleMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )

            FeedSearchRecommendedBusiness(
                recommendedBusiness = RecommendedBusiness(
                    user = RecommendedBusinessUser(
                        id = 0,
                        fullName = "Frizeria Figaro",
                        username = "frizeria_figaro",
                        avatar = null,
                        profession = "Frizerie",
                        ratingsAverage = 4.8f
                    ),
                    distance = 1.2f,
                    isOpen = true
                ),
                onNavigateToUserProfile = {}
            )

            FeedSearchRecommendedBusiness(
                recommendedBusiness = RecommendedBusiness(
                    user = RecommendedBusinessUser(
                        id = 0,
                        fullName = "Frizeria Busuresti",
                        username = "frizeria_bucuresti",
                        avatar = null,
                        profession = "Frizerie",
                        ratingsAverage = 4.2f,
                    ),
                    distance = 2.3f,
                    isOpen = true
                ),
                onNavigateToUserProfile = {}
            )

            MainButtonOutlined(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BasePadding),
                shape = ShapeDefaults.Small,
                title = "Vezi mai mult",
                onClick = {}
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = BasePadding)
                    .padding(top = BasePadding, bottom = SpacingXS),
                text = "Utilizatori",
                style = titleMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            UserSocialItem(
                isFollowedOverrides = false,
                userSocial = UserSocial(
                    id = 1,
                    fullName = "Cristian Ionel",
                    username = "@cristian_ionel",
                    avatar = null,
                    isFollow = false,
                    profession = "Creator",
                    ratingsAverage = 4.5f,
                    isBusinessOrEmployee = false
                ),
                enabled = true,
                onFollow = {},
                onNavigateUserProfile = {}
            )
            UserSocialItem(
                isFollowedOverrides = false,
                userSocial = UserSocial(
                    id = 1,
                    fullName = "Cristian Ionel",
                    username = "@cristian_ionel",
                    avatar = null,
                    isFollow = false,
                    profession = "Creator",
                    ratingsAverage = 4.5f,
                    isBusinessOrEmployee = false
                ),
                enabled = true,
                onFollow = {},
                onNavigateUserProfile = {}
            )

            Spacer(Modifier.height(SpacingXS))

            MainButtonOutlined(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BasePadding),
                shape = ShapeDefaults.Small,
                title = "Vezi mai mult",
                onClick = {}
            )
        }

//        LazyVerticalGrid(
//            columns = GridCells.Fixed(3),
//            verticalArrangement = Arrangement.spacedBy(1.dp),
//            horizontalArrangement = Arrangement.spacedBy(1.dp),
//            modifier = Modifier.fillMaxSize()
//        ) {
//            items(30) { index ->
//                Box(modifier = Modifier
//                    .aspectRatio(9f / 12f)
//                    .background(SurfaceBG)
//                ) {
//                    AsyncImage(
//                        model = "",
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier.fillMaxSize()
//                    )
//
//                    Box(modifier = Modifier
//                        .fillMaxSize()
//                        .background(
//                            brush = Brush.verticalGradient(
//                                colors = listOf(
//                                    Color.Black.copy(alpha = 0.2f),
//                                    Color.Transparent,
//                                    Color.Black.copy(alpha = 0.4f)
//                                )
//                            )
//                        )
//                    )
//                }
//            }
    //    }
    }
}