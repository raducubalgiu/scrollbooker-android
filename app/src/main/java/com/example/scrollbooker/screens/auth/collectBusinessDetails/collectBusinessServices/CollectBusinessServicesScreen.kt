package com.example.scrollbooker.screens.auth.collectBusinessDetails.collectBusinessServices

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.shared.service.domain.model.Service
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge

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
            id = 3,
            name = "Barba",
            businessDomainId = 1
        ),
        Service(
            id = 4,
            name = "Vopsit",
            businessDomainId = 1
        )
    )

    var selectedServicesIds = remember { mutableStateListOf<Int>() }

    FormLayout(
        headLine = stringResource(id = R.string.services),
        subHeadLine = stringResource(id = R.string.addYourBusinessServices),
        buttonTitle = stringResource(id = R.string.nextStep),
        onBack = onBack,
        onNext = onNext,
    ) {
        LazyColumn {
            itemsIndexed(services) { index, service ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(Background)
                        .clickable {
                            if(selectedServicesIds.contains(service.id)) {
                                selectedServicesIds.remove(service.id)
                            } else {
                                selectedServicesIds.add(service.id)
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(start = SpacingXXL),
                        text = service.name,
                        style = bodyLarge,
                        color = OnBackground
                    )
                    Checkbox(
                        modifier = Modifier.padding(end = SpacingXXL),
                        checked = service.id in selectedServicesIds,
                        onCheckedChange = null,
                        colors = CheckboxColors(
                            checkedCheckmarkColor = Color.White,
                            uncheckedCheckmarkColor = Color.Transparent,
                            checkedBoxColor = Primary,
                            uncheckedBoxColor = Color.Transparent,
                            disabledCheckedBoxColor = Divider,
                            disabledUncheckedBoxColor = Divider,
                            disabledIndeterminateBoxColor = Divider,
                            checkedBorderColor = Primary,
                            uncheckedBorderColor = Color.Gray,
                            disabledBorderColor = Divider,
                            disabledUncheckedBorderColor = Divider,
                            disabledIndeterminateBorderColor = Divider
                        )
                    )
                }

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