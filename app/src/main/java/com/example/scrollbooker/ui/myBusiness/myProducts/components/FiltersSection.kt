package com.example.scrollbooker.ui.myBusiness.myProducts.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputCheckbox
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.enums.FilterTypeEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.ui.myBusiness.myProducts.FilterSelection
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium
import kotlin.collections.orEmpty

@Composable
fun FiltersSection(
    isVisible: Boolean,
    filters: List<Filter>,
    selectedFilters: Map<Int, FilterSelection>,
    isLoadingFilters: Boolean,
    actions: FiltersActions
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            shape = RoundedCornerShape(16.dp),
            color = SurfaceBG
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.filters),
                    style = titleMedium
                )

                Spacer(Modifier.height(BasePadding))

                filters.map { filter ->
                    when(filter.type) {
                        FilterTypeEnum.OPTIONS -> {
                            if(filter.singleSelect) {
                                val options = filter.subFilters.map {
                                    Option(value = it.id.toString(), name = it.name)
                                }

                                val selectedSubFilterIds = selectedFilters[filter.id]
                                val selectedOption = (selectedSubFilterIds as? FilterSelection.Options)
                                    ?.ids
                                    ?.firstOrNull()
                                    ?.toString()
                                    ?: ""

                                Column {
                                    Text(
                                        text = filter.name,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Gray
                                    )

                                    Spacer(Modifier.height(SpacingS))

                                    InputSelect(
                                        label = filter.name,
                                        placeholder = "Selecteaza filtrul",
                                        selectedOption = selectedOption,
                                        options = options,
                                        displayLabel = false,
                                        onValueChange = { value ->
                                            //focusManager.clearFocus()

                                            actions.singleOption(filter.id, value?.toInt())
                                        },
                                        isLoading = isLoadingFilters,
                                        isEnabled = !isLoadingFilters,
                                        background = Background,
                                        color = OnBackground
                                    )
                                }
                            } else {
                                Column {
                                    Text(
                                        text = filter.name,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Gray
                                    )

                                    Spacer(Modifier.height(SpacingS))

                                    Column(
                                        modifier = Modifier
                                            .heightIn(max = 110.dp + SpacingS)
                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        filter.subFilters.forEach { sub ->
                                            val selectedSubFilterIds: Set<Int> =
                                                (selectedFilters[filter.id] as? FilterSelection.Options)
                                                    ?.ids
                                                    .orEmpty()

                                            val isChecked = sub.id in selectedSubFilterIds

                                            InputCheckbox(
                                                modifier = Modifier.clip(shape = ShapeDefaults.Medium),
                                                height = 55.dp,
                                                checked = isChecked,
                                                headLine = sub.name,
                                                onCheckedChange = { actions.toggleMultiOption(filter.id, sub.id) }
                                            )

                                            Spacer(Modifier.height(SpacingS))
                                        }
                                    }
                                }
                            }

                            Spacer(Modifier.height(BasePadding))
                        }
                        FilterTypeEnum.RANGE -> {
                            val range = selectedFilters[filter.id] as? FilterSelection.Range
                            val fromText = range?.minim?.toString().orEmpty()
                            val toText = range?.maxim?.toString().orEmpty()
                            val isNotApplicable = (selectedFilters[filter.id] as? FilterSelection.Range)?.isNotApplicable == true

                            FilterRangeSection(
                                filterName = filter.name,
                                unit = filter.unit,
                                minim = fromText,
                                maxim = toText,
                                isNotApplicable = isNotApplicable,
                                onMinim = {
                                    val minim = it.toBigDecimalOrNull()
                                    val maxim = (selectedFilters[filter.id] as? FilterSelection.Range)?.maxim

                                    actions.setRange(filter.id, minim, maxim)
                                },
                                onMaxim = {
                                    val minim = (selectedFilters[filter.id] as? FilterSelection.Range)?.minim
                                    val maxim = it.toBigDecimalOrNull()

                                    actions.setRange(filter.id, minim, maxim)
                                },
                                onIsApplicable = { actions.toggleApplicable(filter.id) }
                            )
                        }
                        null -> Unit
                    }
                }
            }
        }
    }

    Spacer(Modifier.height(BasePadding))
}