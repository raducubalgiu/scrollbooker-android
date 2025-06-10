package com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Layout
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.MainButton
import com.example.scrollbooker.core.util.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.LoadingScreen
import com.example.scrollbooker.core.util.MessageScreen

@Composable
fun EmploymentRequestsScreen(
    viewModel: EmploymentRequestsViewModel,
    onBack: () -> Unit,
    onNavigateSelectEmployee: () -> Unit
) {
    Layout(
        headerTitle = stringResource(R.string.employmentRequests),
        onBack = onBack
    ) {
        val state by viewModel.employmentRequests.collectAsState()

        Column(Modifier.fillMaxSize()) {
            when(state) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Success -> {
                    val employmentRequests = (state as FeatureState.Success).data

                    if(employmentRequests.isEmpty()) {
                        MessageScreen(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Outlined.CollectionsBookmark,
                            message = stringResource(R.string.notFoundEmploymentRequests),
                        )
                    } else {
                        LazyColumn(Modifier.weight(1f)) {
                            items(employmentRequests) { employmentRequest ->
                                Text(employmentRequest.status)
                            }
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
}