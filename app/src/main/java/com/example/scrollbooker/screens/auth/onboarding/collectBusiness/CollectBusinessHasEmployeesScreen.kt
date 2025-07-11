package com.example.scrollbooker.screens.auth.onboarding.collectBusiness

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyLarge

data class EmployeeConfirm(
    val confirm: Boolean,
    val label: String
)

@Composable
fun CollectBusinessHasEmployeesScreen(
    viewModel: CollectBusinessHasEmployeesViewModel,
    onNext: () -> Unit
) {
    val businessState by viewModel.businessState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    val isError = businessState is FeatureState.Error
    val isLoading = isSaving is FeatureState.Loading

    val confirmation = listOf(
        EmployeeConfirm(confirm = true, label = stringResource(R.string.YesIHaveEmployees)),
        EmployeeConfirm(confirm = false, label = stringResource(R.string.NoIDoNotHaveEmployees)),
    )

    val verticalScroll = rememberScrollState()

    FormLayout(
        headLine = stringResource(R.string.doYouHaveEmployees),
        subHeadLine = "",
        buttonTitle = stringResource(R.string.nextStep),
        isEnabled = !isError && !isLoading,
        isLoading = isLoading,
        enableBack = false,
        onNext = onNext
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(verticalScroll)
        ) {
            Column(Modifier.padding(horizontal = SpacingXXL)) {
                Text(
                    style = bodyLarge,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    text = stringResource(R.string.ifHaveEmployeesDescription),
                )
                Spacer(Modifier.height(BasePadding))
                Text(
                    style = bodyLarge,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    text = stringResource(R.string.ifNotHaveEmployeesDescription)
                )
            }

            Spacer(Modifier.height(SpacingXXL))

            HorizontalDivider(color = Divider, thickness = 0.5.dp)

            when(val state = businessState) {
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Success -> {
                    val business = state.data

                    confirmation.forEachIndexed { index, conf ->
                        InputRadio(
                            modifier = Modifier
                                .height(85.dp),
                            headLine = conf.label,
                            selected = conf.confirm == business.hasEmployees,
                            onSelect = { viewModel.updateBusinessHasEmployees(conf.confirm) }
                        )

                        if(index == 0) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = SpacingXXL)
                                    .height(0.55.dp)
                                    .background(Divider.copy(alpha = 0.5f))
                            )
                        }
                    }
                }
            }
        }
    }
}