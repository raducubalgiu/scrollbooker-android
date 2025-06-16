package com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.flow.selectEmployee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.customized.listItems.SelectableUserItem
import com.example.scrollbooker.components.core.inputs.SearchBar
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.LoadingScreen
import com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.flow.EmploymentRequestViewModel
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
@Composable
fun EmploymentSelectEmployeeScreen(
    globalViewModel: EmploymentRequestViewModel,
    localViewModel: EmploymentSelectEmployeeViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val usersState by localViewModel.searchUsersClientsState.collectAsState()
    val searchQuery by localViewModel.searchQuery.collectAsState()
    val employeeId by globalViewModel.employeeId.collectAsState()

    Layout(
        headerTitle = stringResource(R.string.selectEmployee),
        onBack = onBack
    ) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                SearchBar(
                    value = searchQuery,
                    onValueChange = { localViewModel.onSearchQueryChanged(it) },
                    placeholder = stringResource(R.string.searchEmployee)
                )
                Spacer(Modifier.height(SpacingS))

                when(usersState) {
                    is FeatureState.Loading -> LoadingScreen()
                    is FeatureState.Error -> ErrorScreen()
                    is FeatureState.Success<*> -> {
                        val users = (usersState as FeatureState.Success).data

                        LazyColumn {
                            items(users) { user ->
                                SelectableUserItem(
                                    user = user,
                                    enablePaddingH = false,
                                    isSelected = user.id == employeeId,
                                    onClick = { globalViewModel.assignEmployee(user.id) }
                                )
                            }
                        }
                    }
                    else -> Unit
                }
            }
            MainButton(
                enabled = employeeId != null,
                title = stringResource(R.string.nextStep),
                onClick = onNext
            )
        }
    }
}