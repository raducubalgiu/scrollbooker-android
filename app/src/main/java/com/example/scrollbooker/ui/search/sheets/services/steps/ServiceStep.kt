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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.components.SearchAdvancedFilters
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.headlineMedium
import com.example.scrollbooker.ui.theme.headlineSmall
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ServiceStep(
    viewModel: SearchViewModel,
    services: FeatureState<List<Service>>?,
    serviceFilters: FeatureState<List<Filter>>?,
    selectedFilters: Map<Int, Int>,
    selectedServiceDomain: ServiceDomain?,
    onSetSelectedFilter: (Int, Int) -> Unit,
    onBack: () -> Unit,
    onConfirm: (Int?) -> Unit
) {
    var localSelectedServiceId by remember { mutableStateOf("") }

    LaunchedEffect(localSelectedServiceId) {
        if(localSelectedServiceId.isNotEmpty()) {
            viewModel.setServiceId(localSelectedServiceId.toInt())
        }
    }

    val servicesOptions = when(val state = services) {
        is FeatureState.Success -> state.data.map { s ->
            Option(
                value = s.id.toString(),
                name = s.displayName
            )
        }
        else -> emptyList()
    }

    Column(modifier = Modifier.padding(vertical = SpacingS)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomIconButton(
                boxSize = 60.dp,
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = onBack
            )

            Text(
                text = "Filtrare avansata",
                style = titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier
                .padding(horizontal = BasePadding)
                .size(130.dp)
                .clip(CircleShape)
                .background(SurfaceBG),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = selectedServiceDomain?.thumbnailUrl,
                    contentDescription = "Selected image",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(BasePadding))

            Text(
                text = selectedServiceDomain?.name ?: "",
                style = headlineSmall,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(BasePadding))

        Column(modifier = Modifier
            .weight(1f)
            .padding(horizontal = BasePadding)
        ) {
            InputSelect(
                options = servicesOptions,
                selectedOption = localSelectedServiceId,
                placeholder = stringResource(R.string.chooseService),
                onValueChange = { localSelectedServiceId = it.toString() },
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(BasePadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                TextButton(
                    onClick = {},
                    enabled = true,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = OnBackground,
                    )
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        style = titleMedium,
                    )
                }
            }

            Button(
                contentPadding = PaddingValues(
                    vertical = BasePadding,
                    horizontal = SpacingXXL
                ),
                onClick = { onConfirm(localSelectedServiceId.toIntOrNull()) },
                enabled = true
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.confirm),
                        style = titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}