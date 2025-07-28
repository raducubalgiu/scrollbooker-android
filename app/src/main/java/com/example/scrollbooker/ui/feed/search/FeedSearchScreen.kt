package com.example.scrollbooker.ui.feed.search
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.search.data.remote.SearchTypeEnum
import com.example.scrollbooker.entity.search.domain.model.RecentlySearch
import com.example.scrollbooker.entity.search.domain.model.UserSearch
import com.example.scrollbooker.navigation.LocalRootNavController
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.feed.components.FeedSearchRecommendedBusiness
import com.example.scrollbooker.ui.feed.components.search.FeedSearchRecentlyHistory
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.delay

@Composable
fun FeedSearchScreen(
    viewModel: FeedSearchViewModel,
    userSearch: FeatureState<UserSearch>,
    onBack: () -> Unit,
    onGoToSearch: () -> Unit,
    onCreateUserSearch: (String) -> Unit,
    onDeleteRecentlySearch: (Int) -> Unit
) {
    val rootNavController = LocalRootNavController.current
    val searchState by viewModel.searchState.collectAsState()

    var query by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(200)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    fun handleNavigateToUserProfile(userId: Int) {
        keyboardController?.hide()
        rootNavController.navigate(
            "${MainRoute.UserProfile.route}/$userId"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .safeDrawingPadding()
    ) {
        FeedSearchHeader(
            value = query,
            modifier = Modifier.focusRequester(focusRequester),
            onValueChange = {
                query = it
                viewModel.handleSearch(it)
            },
            onSearch = {
                keyboardController?.hide()
                onCreateUserSearch(query)
                onGoToSearch()
            },
            onClearInput = {
                query = ""
                viewModel.clearSearch()
            },
            onClick = { keyboardController?.show() },
            onBack = {
                keyboardController?.hide()
                onBack()
            },
        )

        Column(Modifier.fillMaxSize()) {
            if(query.isEmpty()) {
                when(val userSearchState = userSearch) {
                    is FeatureState.Error -> ErrorScreen()
                    is FeatureState.Loading -> LoadingScreen()
                    is FeatureState.Success -> {
                        var firstRecently = userSearchState.data.recentlySearch.take(3)
                        val allRecently = userSearchState.data.recentlySearch
                        var recently by rememberSaveable { mutableStateOf<List<RecentlySearch>>(firstRecently) }
                        val isExpanded = recently.size > 3

                        LazyColumn(modifier = Modifier.padding(top = SpacingS)) {
                            item {
                                recently.map { rec ->
                                    FeedSearchRecentlyHistory(
                                        recentlySearch = rec,
                                        onDeleteRecentlySearch = onDeleteRecentlySearch
                                    )
                                }

                                if(allRecently.size > 3) {
                                    Box(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(BasePadding)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            onClick = {
                                                recently =
                                                    if (isExpanded) firstRecently else allRecently
                                            }
                                        ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row {
                                            Text(
                                                text = if(isExpanded) "Inchide" else "Vezi mai mult",
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.Gray,
                                                style = bodyLarge
                                            )
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowDown,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }

                                Text(
                                    modifier = Modifier.padding(BasePadding),
                                    text = "Servicii in apropiere",
                                    style = titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp
                                )
                            }

                            items(userSearchState.data.recommendedBusiness) { recommendedBusiness ->
                                FeedSearchRecommendedBusiness(
                                    recommendedBusiness = recommendedBusiness,
                                    onNavigateToUserProfile = { handleNavigateToUserProfile(it) }
                                )
                            }
                        }
                    }
                }
            } else {
                when(val search = searchState) {
                    is FeatureState.Error -> {
                        ErrorScreen(
                            modifier = Modifier.padding(top = 50.dp),
                            arrangement = Arrangement.Top
                        )
                    }
                    is FeatureState.Loading -> {
                        LoadingScreen(
                            modifier = Modifier.padding(top = 50.dp),
                            arrangement = Arrangement.Top
                        )
                    }
                    is FeatureState.Success -> {
                        LazyColumn(modifier = Modifier
                            .weight(1f)
                            .padding(top = SpacingS)
                        ) {
                            item {
                                if(query.isNotEmpty()) {
                                    FeedSearchKeyword(keyword = query)
                                }
                            }

                            itemsIndexed(search.data) { index, searchResult ->
                                when(searchResult.type) {
                                    SearchTypeEnum.USER -> searchResult.user?.let {
                                        FeedSearchUserItem(
                                            user = searchResult.user,
                                            onNavigateToUserProfile = { handleNavigateToUserProfile(it) }
                                        )
                                    }
                                    SearchTypeEnum.SERVICE -> {
                                        FeedSearchKeyword(
                                            keyword = searchResult.label,
                                            icon = R.drawable.ic_shopping_outline
                                        )
                                    }
                                    SearchTypeEnum.BUSINESS_TYPE -> {
                                        FeedSearchKeyword(
                                            keyword = searchResult.label,
                                            icon = R.drawable.ic_store_solid
                                        )
                                    }
                                    SearchTypeEnum.KEYWORD -> {
                                        FeedSearchKeyword(
                                            keyword = searchResult.label,
                                        )
                                    }
                                    else -> Unit
                                }
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
}