package com.example.scrollbooker.ui.myBusiness.myEmploymentRequests
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.dialog.DialogConfirm
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.myBusiness.myEmploymentRequests.components.EmploymentRequestCard
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun EmploymentRequestsScreen(
    viewModel: EmploymentRequestsViewModel,
    onBack: () -> Unit,
    onNavigateSelectEmployee: () -> Unit
) {
    val state by viewModel.employmentRequests.collectAsState()
    var selectedEmployment by remember { mutableStateOf<Int?>(null) }
    var openModal by remember { mutableStateOf(false) }

    if(openModal) {
        DialogConfirm(
            onDismissRequest = {
                openModal = false
                selectedEmployment = null
            },
            onConfirmation = {},
            title = "Stergere cerere",
            text = "Esti sigur ca vrei sa stergi aceasta cerere de angajare?",
            confirmText = "Sterge"
        )
    }

    Layout(
        headerTitle = stringResource(R.string.employmentRequests),
        onBack = onBack
    ) {
        Column(Modifier.fillMaxSize()) {
            when(val result = state) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Success -> {
                    LazyColumn(Modifier.weight(1f)) {
                        item { Spacer(Modifier.height(SpacingS)) }

                        itemsIndexed(result.data) { index, employmentRequest ->
                            EmploymentRequestCard(
                                employmentRequest = employmentRequest,
                                onClick = {
                                    openModal = true
                                    selectedEmployment = it
                                }
                            )

                            if(index < result.data.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = SpacingXL),
                                    color = Divider,
                                    thickness = 0.55.dp
                                )
                            }
                        }
                    }
                }
            }

            Column {
                HorizontalDivider(color = Divider, thickness = 0.55.dp)
                Spacer(Modifier.height(BasePadding))
                MainButton(
                    modifier = Modifier.padding(bottom = BasePadding),
                    title = stringResource(R.string.sendAnEmploymentRequest),
                    onClick = onNavigateSelectEmployee,
                )
            }
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