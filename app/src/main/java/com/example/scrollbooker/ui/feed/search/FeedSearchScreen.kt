package com.example.scrollbooker.ui.feed.search
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun FeedSearchScreen(
    viewModel: FeedSearchViewModel,
    onBack: () -> Unit,
    onNavigateToUserProfile: (Int, String) -> Unit,
) {
    val currentSearch by viewModel.currentSearch.collectAsState()
    val searchState by viewModel.searchState.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(top = 6.dp)
            .statusBarsPadding()
    ) {
        FeedSearchHeader(
            value = currentSearch,
            modifier = Modifier.focusRequester(focusRequester),
            onValueChange = viewModel::handleSearch,
            onClearInput = viewModel::clearSearch,
            onClick = { keyboardController?.show() },
            onBack = {
                keyboardController?.hide()
                onBack()
            },
        )

        when(searchState) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Success -> {
                val users = (searchState as FeatureState.Success).data

                LazyColumn(Modifier.weight(1f)) {
                    items(users) { user ->
                        ListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = { onNavigateToUserProfile(user.id, user.username) }
                                ),
                            headlineContent = {
                                Text(
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(bottom = SpacingXXS),
                                    style = titleMedium,
                                    color = OnBackground,
                                    text = user.fullName
                                )
                            },
                            supportingContent = {
                                Text(
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = bodyMedium,
                                    text = if(user.isBusinessOrEmployee) user.profession
                                    else "@${user.username}"
                                )
                            },
                            leadingContent = {
                                if(user.isBusinessOrEmployee) {
                                    AvatarWithRating(
                                        url = user.avatar ?: "",
                                        rating = user.ratingsAverage,
                                        size = 52.5.dp,
                                        elevation = 2.dp,
                                        onClick = {}
                                    )
                                } else {
                                    Avatar(
                                        url = user.avatar ?: "",
                                        size = 52.5.dp
                                    )
                                }
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = Background
                            )
                        )
                    }

                    item {
                        if(users.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(BasePadding),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = stringResource(R.string.notFoundAnyResult))
                            }
                        }
                    }
                }
            }
            null -> Unit
        }
    }
}