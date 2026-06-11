package com.example.scrollbooker.ui.booking
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.components.core.headers.Header
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.booking.services.BookingProductsList
import com.example.scrollbooker.ui.booking.services.BookingServicesTabs
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.headlineMedium
import com.example.scrollbooker.ui.theme.titleLarge
import kotlinx.coroutines.launch

@Composable
fun BookingServicesScreen(
    modifier: Modifier = Modifier,
    viewModel: BookingViewModel,
    onNavigateToSpecialists: () -> Unit,
    onBack: () -> Unit
) {
    val productsState by viewModel.productsState.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = { Header(onBack = onBack) },
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when (val state = productsState) {
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Success -> {
                    val serviceGroups = state.data.data

                    val activeTabIndex by remember(serviceGroups) {
                        derivedStateOf {
                            val firstVisibleIndex = listState.firstVisibleItemIndex
                            if (firstVisibleIndex == 0) 0 else firstVisibleIndex - 1
                        }
                    }

                    Column(modifier = Modifier.fillMaxSize()) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                modifier = Modifier.padding(horizontal = BasePadding),
                                text = stringResource(R.string.chooseServices),
                                style = headlineMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(SpacingS))

                            if (serviceGroups.isNotEmpty()) {
                                BookingServicesTabs(
                                    activeTabIndex = activeTabIndex,
                                    onTabChange = { tabIndex ->
                                        scope.launch {
                                            listState.animateScrollToItem(tabIndex + 1)
                                        }
                                    },
                                    serviceGroups = serviceGroups
                                )
                            }

                            BookingProductsList(
                                state = listState,
                                serviceGroups = serviceGroups
                            )
                        }

                        AnimatedVisibility(
                            visible = true
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(BasePadding),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        text = "260 RON",
                                        style = titleLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(Modifier.height(SpacingXXS))

                                    Text(
                                        text = "30min",
                                        style = bodyMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = Color.Gray
                                    )
                                }

                                Button(
                                    onClick = {},
                                    contentPadding = PaddingValues(
                                        vertical = BasePadding,
                                        horizontal = SpacingXL
                                    )
                                ) {
                                    Text(
                                        style = bodyLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        text = stringResource(R.string.next),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}