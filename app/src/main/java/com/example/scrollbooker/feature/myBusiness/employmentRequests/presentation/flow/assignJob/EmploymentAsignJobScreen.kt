package com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.flow.assignJob

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.inputs.InputRadio
import com.example.scrollbooker.components.inputs.Option
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.LoadingScreen
import com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.list.EmploymentRequestsViewModel

@Composable
fun EmploymentAssignJobScreen(
    globalViewModel: EmploymentRequestsViewModel,
    localViewModel: EmploymentAssignJobViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val professionsState by localViewModel.professionsState.collectAsState()
    val assignedProfession by globalViewModel.assignedProfession.collectAsState()

    Layout(
        headerTitle = stringResource(R.string.assignJob),
        onBack = onBack,
        enablePadding = false
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(vertical = BasePadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                when(professionsState) {
                    is FeatureState.Loading -> LoadingScreen()
                    is FeatureState.Error -> ErrorScreen()
                    is FeatureState.Success -> {
                        val professions = (professionsState as FeatureState.Success).data
                        val options = professions.map { Option(value = it.id.toString(), name = it.name) }

                        InputRadio(
                            value = assignedProfession.toString(),
                            options = options,
                            onValueChange = { globalViewModel.assignProfession(it) }
                        )
                    }
                }
            }
            MainButton(
                modifier = Modifier.padding(horizontal = BasePadding),
                title = stringResource(R.string.nextStep),
                onClick = onNext,
                enabled = assignedProfession != null
            )
        }
    }
}