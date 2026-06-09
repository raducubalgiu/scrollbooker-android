package com.example.scrollbooker.ui.myBusiness.myEmployees
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.myBusiness.myEmployees.tabs.MyEmployeesTab
import com.example.scrollbooker.ui.myBusiness.myEmployees.tabs.employeesTab.EmployeesTab
import com.example.scrollbooker.ui.myBusiness.myEmployees.tabs.employmentRequestsTab.EmploymentRequestsTab
import com.example.scrollbooker.ui.shared.products.components.ServiceTab
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import kotlinx.coroutines.launch

@Composable
fun MyEmployeesScreen(
    viewModel: MyEmployeesViewModel,
    onBack: () -> Unit,
    onNavigateToSearchUser: () -> Unit
) {
    val pagerState = rememberPagerState { 2 }
    val tabs = remember { MyEmployeesTab.getTabs }
    val selectedTabIndex = pagerState.currentPage
    val scope = rememberCoroutineScope()

    Layout(
        headerTitle = stringResource(R.string.employees),
        onBack = onBack,
        enablePaddingH = false
    ) {
        Column(Modifier.fillMaxSize()) {
            ScrollableTabRow(
                containerColor = Background,
                contentColor = OnSurfaceBG,
                edgePadding = BasePadding,
                selectedTabIndex = pagerState.currentPage,
                indicator = {},
                divider = {
                    HorizontalDivider(
                        modifier = Modifier.padding(top = 5.dp),
                        color = Divider,
                        thickness = 0.55.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = selectedTabIndex == index

                    ServiceTab(
                        isSelected = isSelected,
                        serviceName = stringResource(tab.label),
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        }
                    )
                }
            }

            HorizontalPager(state = pagerState) { index ->
                when(index) {
                    0 -> EmployeesTab(viewModel)
                    1 -> EmploymentRequestsTab(viewModel, onNavigateToSearchUser)
                }
            }
        }
    }
}