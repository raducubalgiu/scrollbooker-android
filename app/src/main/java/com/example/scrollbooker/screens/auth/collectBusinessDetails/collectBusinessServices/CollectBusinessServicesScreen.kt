package com.example.scrollbooker.screens.auth.collectBusinessDetails.collectBusinessServices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputCheckbox
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.screens.auth.collectBusinessDetails.CollectBusinessDetails
import com.example.scrollbooker.shared.service.domain.model.Service
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun CollectBusinessServicesScreen(
    viewModel: CollectBusinessServicesViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val services = listOf(
        Service(
            id = 1,
            name = "Tuns",
            businessDomainId = 1
        ),
        Service(
            id = 2,
            name = "Pensat",
            businessDomainId = 1
        ),
        Service(
            id = 1,
            name = "Barba",
            businessDomainId = 1
        ),
        Service(
            id = 1,
            name = "Vopsit",
            businessDomainId = 1
        )
    )

    CollectBusinessDetails(
        headLine = stringResource(id = R.string.services),
        subHeadLine = stringResource(id = R.string.addYourBusinessServices),
        onBack = onBack,
        onNext = onNext,
    ) {
        LazyColumn(Modifier.padding(horizontal = BasePadding)) {
            itemsIndexed(services) { index, service ->
                InputCheckbox(
                    background = Color.Transparent,
                    checked = false,
                    onCheckedChange = {},
                    headLine = service.name
                )

                if(index < services.lastIndex) {
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