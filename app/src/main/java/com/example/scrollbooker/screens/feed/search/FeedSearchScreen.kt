package com.example.scrollbooker.screens.feed.search
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.SearchBarSmall
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.search.data.remote.SearchTypeEnum
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FeedSearchScreen(
    viewModel: FeedSearchViewModel,
    onBack: () -> Unit,
    onGoToSearch: () -> Unit
) {
    val searchState by viewModel.searchState.collectAsState()
    var query by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(150)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    DisposableEffect(Unit) {
        onDispose {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = BasePadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.clickable(
                onClick = {
                    coroutineScope.launch {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        delay(250)
                        onBack()
                    }
                },
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
            SearchBarSmall(
                value = query,
                onValueChange = {
                    query = it
                    viewModel.handleSearch(it, lat = 44.450507.toFloat(), lng = 25.993102.toFloat())
                },
                onSearch = {
                    keyboardController?.hide()
                    onGoToSearch()
                },
                readOnly = false,
                onClearInput = {
                    query = ""
                    viewModel.clearSearch()
                },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                )
        }

        Column(Modifier.fillMaxSize()) {
            when(val search = searchState) {
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Success -> {
                    LazyColumn(modifier = Modifier
                        .weight(1f)
                        .padding(top = SpacingS)
                    ) {
                        item {
                           if(query.isNotEmpty()) {
                               FeedSearchKeyword(query, displayRightIcon = false)
                           }
                        }

                        itemsIndexed(search.data) { index, searchResult ->
                            when(searchResult.type) {
                                SearchTypeEnum.USER -> {
                                    if(searchResult.user != null) {
                                        FeedSearchUserItem(user = searchResult.user)
                                    }
                                }
                                SearchTypeEnum.SERVICE -> {
                                    FeedSearchKeyword(searchResult.label, R.drawable.ic_shopping_outline)
                                }
                                SearchTypeEnum.BUSINESS_TYPE -> {
                                    FeedSearchKeyword(searchResult.label, R.drawable.ic_store_solid)
                                }
                                SearchTypeEnum.KEYWORD -> FeedSearchKeyword(searchResult.label)
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