package com.example.scrollbooker.ui.myBusiness.myEmployees
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.ui.myBusiness.myEmployees.components.EmployeeCard

@Composable
fun EmployeesScreen(
    viewModel: EmployeesViewModel,
    onBack: () -> Unit,
    onNavigateToDismissalScreen: () -> Unit
) {
    val employees = viewModel.employees.collectAsLazyPagingItems()
    val refreshState = employees.loadState.refresh

    Layout(
        headerTitle = stringResource(R.string.employees),
        onBack = onBack,
        enablePaddingH = false
    ) {
        Column(Modifier.fillMaxSize()) {
            when(refreshState) {
                is LoadState.Loading -> LoadingScreen()
                is LoadState.Error -> ErrorScreen()
                is LoadState.NotLoading -> {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(employees.itemCount) { index ->
                            employees[index]?.let {
                                EmployeeCard(it, onNavigateToDismissalScreen)
                            }
                        }

                        item {
                            when(employees.loadState.append) {
                                is LoadState.Loading -> LoadMoreSpinner()
                                is LoadState.Error -> { Text("Ceva nu a mers cum trebuie") }
                                is LoadState.NotLoading -> Unit
                            }
                        }
                    }
                }
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        if(refreshState is LoadState.NotLoading && employees.itemCount == 0) {
            EmptyScreen(
                message = stringResource(R.string.notFoundEmployees),
                icon = painterResource(R.drawable.ic_users_outline)
            )
        }
    }
}