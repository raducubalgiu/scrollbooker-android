package com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests.list
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen

@Composable
fun EmploymentRequestsScreen(
    viewModel: EmploymentRequestsViewModel,
    onBack: () -> Unit,
    onNavigateSelectEmployee: () -> Unit
) {
    val state by viewModel.employmentRequests.collectAsState()

    Layout(
        modifier = Modifier.statusBarsPadding(),
        headerTitle = stringResource(R.string.employmentRequests),
        onBack = onBack
    ) {
        Column(Modifier.fillMaxSize()) {
            when(val result = state) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Success -> {
                    LazyColumn(Modifier.weight(1f)) {
                        items(result.data) { employmentRequest ->
                            Text(employmentRequest.status)
                        }
                    }
                }
            }

            MainButton(
                title = stringResource(R.string.sendAnEmploymentRequest),
                onClick = onNavigateSelectEmployee,
            )
        }
    }

    when(val result = state) {
        is FeatureState.Success -> {
            if(result.data.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    MessageScreen(
                        icon = painterResource(R.drawable.ic_business_outline),
                        message = stringResource(R.string.notFoundEmploymentRequests),
                    )
                }
            }
        }
        else -> Unit
    }
}