package com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.FormLayout
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun EmploymentAssignJobScreen(
    viewModel: EmploymentRequestsViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val professionsState by viewModel.professionsState.collectAsState()
    val selectedProfession by viewModel.selectedProfession.collectAsState()

    FormLayout(
        modifier = Modifier.safeDrawingPadding(),
        headLine = stringResource(R.string.assignJob),
        subHeadLine = stringResource(R.string.chooseProfessionsFromTheList),
        onBack = onBack,
        onNext = onNext,
        isEnabled = selectedProfession != null,
        buttonTitle = stringResource(R.string.nextStep)
    ) {
        when(professionsState) {
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Success -> {
                val professions = (professionsState as FeatureState.Success).data

                LazyColumn {
                    itemsIndexed(professions) { index, profession ->
                        InputRadio(
                            selected = profession.id == selectedProfession?.id,
                            onSelect = { viewModel.setSelectedProfession(profession) },
                            headLine = profession.name
                        )

                        if(index < professions.size) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = SpacingXL),
                                color = Divider,
                                thickness = 0.55.dp
                            )
                        }
                    }
                }
            }
        }
    }
}