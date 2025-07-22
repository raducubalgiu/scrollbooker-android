package com.example.scrollbooker.ui.profile.myProfile.myBusiness.myEmployees

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.ui.profile.myProfile.myBusiness.myEmployees.components.EmployeeCard

@Composable
fun EmployeesScreen(
    viewModel: EmployeesViewModel,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Layout(
        modifier = Modifier.statusBarsPadding(),
        headerTitle = stringResource(R.string.employees),
        onBack = onBack
    ) {
        val employeesState = viewModel.employeesFlow.collectAsLazyPagingItems()

        Column(Modifier.fillMaxSize()) {
            when(employeesState.loadState.refresh) {
                is LoadState.Loading -> LoadingScreen()
                is LoadState.Error -> ErrorScreen()
                is LoadState.NotLoading -> Unit
            }

            LazyColumn {
                items(employeesState.itemCount) { index ->
                    val employee = employeesState[index]
                    if(employee != null) {
                        EmployeeCard(employee, onNavigate)
                    }
                }
            }
        }
    }
}