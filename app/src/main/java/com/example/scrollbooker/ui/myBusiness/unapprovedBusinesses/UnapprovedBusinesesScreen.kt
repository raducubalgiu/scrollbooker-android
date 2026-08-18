package com.example.scrollbooker.ui.myBusiness.unapprovedBusinesses

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import androidx.compose.runtime.getValue
import com.example.scrollbooker.navigation.navigators.ProfileNavigator

@Composable
fun UnapprovedBusinessesScreen(
    viewModel: UnapprovedBusinessesViewModel,
    profileNavigate: ProfileNavigator,
) {
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
    val unapprovedBusinesses = viewModel.unapprovedBusinesses.collectAsLazyPagingItems()
    val refreshState = unapprovedBusinesses.loadState.refresh
    val isInitialLoading = refreshState is LoadState.Loading && unapprovedBusinesses.itemCount == 0

    Scaffold(
        topBar = {
            Header(
                title = stringResource(R.string.unapprovedBusinesses),
                onBack = { profileNavigate.back() }
            )
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                isInitialLoading -> LoadingScreen()
                refreshState is LoadState.Error -> ErrorScreen()
                else -> {
                    if(unapprovedBusinesses.itemCount == 0) {
                        MessageScreen(
                            message = stringResource(R.string.notFoundUnapprovedBusinesses),
                            icon = painterResource(R.drawable.ic_clipboard_check_outline)
                        )
                    } else {
                        UnapprovedBusinessesList(
                            unapprovedBusinesses = unapprovedBusinesses,
                            onRefresh = { viewModel.refresh() },
                            onApprove = { viewModel.approveBusiness(it) },
                            isSaving = isSaving
                        )
                    }
                }
            }
        }
    }
}