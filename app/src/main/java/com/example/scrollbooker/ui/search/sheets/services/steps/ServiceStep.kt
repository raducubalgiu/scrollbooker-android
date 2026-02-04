package com.example.scrollbooker.ui.search.sheets.services.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.ui.search.components.SearchAdvancedFilters
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ServiceStep(
    services: FeatureState<List<Service>>?,
    serviceFilters: FeatureState<List<Filter>>?,
    selectedFilters: Map<Int, Int>,
    selectedOption: String,
    onServiceChanged: (Int?) -> Unit,
    onSetSelectedFilter: (Int, Int) -> Unit,
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    val servicesOptions = when(val state = services) {
        is FeatureState.Success -> state.data.map { s ->
            Option(
                value = s.id.toString(),
                name = s.displayName
            )
        }
        else -> emptyList()
    }

    Column(modifier = Modifier
        .padding(vertical = SpacingS)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomIconButton(
                boxSize = 60.dp,
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = onBack
            )

            Text(
                text = "Par si barba",
                style = titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(BasePadding))

        Column(modifier = Modifier.weight(1f).padding(horizontal = BasePadding)) {
            InputSelect(
                options = servicesOptions,
                selectedOption = selectedOption,
                placeholder = stringResource(R.string.chooseService),
                onValueChange = { onServiceChanged(it?.toInt()) },
                isLoading = services is FeatureState.Loading,
            )

            Spacer(Modifier.height(BasePadding))

            when(val filters = serviceFilters) {
                is FeatureState.Loading -> {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(rememberShimmerBrush())
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
                is FeatureState.Success -> {
                    SearchAdvancedFilters(
                        selectedFilters = selectedFilters,
                        onSetSelectedFilter = { filterId, subFilterId ->
                            onSetSelectedFilter(filterId, subFilterId)
                        },
                        filters = filters.data
                    )
                }
                else -> Unit
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MainButtonOutlined(
                modifier = Modifier.weight(0.5f),
                title = stringResource(R.string.cancel),
                contentPadding = PaddingValues(BasePadding),
                onClick = onBack
            )

            Spacer(Modifier.width(SpacingS))

            MainButton(
                modifier = Modifier.weight(0.5f),
                title = stringResource(R.string.confirm),
                onClick = onConfirm
            )
        }
    }
}