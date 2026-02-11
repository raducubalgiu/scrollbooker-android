package com.example.scrollbooker.ui.profile.tabs.employees
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.rememberFlingBehavior
import com.example.scrollbooker.ui.profile.components.ProfileLayoutViewModel
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun ProfileEmployeesTab(
    viewModel: ProfileLayoutViewModel,
    onNavigateToEmployeeProfile: (Int) -> Unit
) {
    val employees = viewModel.employees.collectAsLazyPagingItems()

    val refreshState = employees.loadState.refresh
    val appendState = employees.loadState.append

    val flingBehaviour = rememberFlingBehavior()

    Column(modifier = Modifier.fillMaxSize()) {
        when(refreshState) {
            is LoadState.Loading -> {
                LoadingScreen(
                    modifier = Modifier.padding(top = 50.dp),
                    arrangement = Arrangement.Top
                )
            }
            is LoadState.Error -> ErrorScreen()
            is LoadState.NotLoading -> {
                if(employees.itemCount == 0) {
                    MessageScreen(
                        modifier = Modifier.padding(top = 50.dp),
                        arrangement = Arrangement.Top,
                        message = stringResource(R.string.dontFoundResults),
                        icon = painterResource(R.drawable.ic_users_outline)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        flingBehavior = flingBehaviour
                    ) {
                        item {
                            Text(
                                modifier = Modifier.padding(BasePadding),
                                text = stringResource(R.string.employees),
                                fontWeight = FontWeight.SemiBold,
                                style = titleLarge
                            )
                        }

                        items(employees.itemCount) { index ->
                            employees[index]?.let { employee ->
                                ProfileEmployee(
                                    employee = employee,
                                    onNavigateToEmployeeProfile = onNavigateToEmployeeProfile,
                                    onOpenServices = {}
                                )

                                if(index < employees.itemCount - 1) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(
                                            start = BasePadding,
                                            end = BasePadding,
                                            top = SpacingM,
                                            bottom = SpacingXS
                                        ),
                                        color = Divider,
                                        thickness = 0.55.dp
                                    )
                                }
                            }
                        }

                        item {
                            when(appendState) {
                                is LoadState.Error -> Text("A aparut o eroare")
                                is LoadState.Loading -> LoadMoreSpinner()
                                is LoadState.NotLoading -> Unit
                            }
                        }

                        item {
                            Spacer(modifier = Modifier
                                .fillMaxSize()
                                .height(
                                    WindowInsets.safeContent
                                        .only(WindowInsetsSides.Bottom)
                                        .asPaddingValues()
                                        .calculateBottomPadding()
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}