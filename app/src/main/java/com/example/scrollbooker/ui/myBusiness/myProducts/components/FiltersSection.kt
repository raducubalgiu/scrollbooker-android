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
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium
import kotlin.collections.orEmpty

@Composable
fun FiltersSection(
    isVisible: Boolean,
    filters: List<Filter>,
    selectedFilters: Map<Int, Set<Int>>,
    isLoadingFilters: Boolean,
    onToggleOption: (filterId: Int, subFilterId: Int, isSingleSelect: Boolean) -> Unit
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

                filters.forEach { filter ->
                    val selectedSubFilterIds = selectedFilters[filter.id].orEmpty()

                    if (filter.singleSelect) {
                        val options = filter.subFilters.map {
                            Option(value = it.id.toString(), name = it.name)
                        }
                        val selectedOption = selectedSubFilterIds.firstOrNull()?.toString() ?: ""

                        Column {
                            Text(
                                text = filter.name,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray
                            )

                            Spacer(Modifier.height(SpacingS))

                            InputSelect(
                                label = filter.name,
                                placeholder = "Selectează filtrul",
                                selectedOption = selectedOption,
                                options = options,
                                displayLabel = false,
                                onValueChange = { value ->
                                    value?.toIntOrNull()?.let { subId ->
                                        onToggleOption(filter.id, subId, true)
                                    }
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
                                    val isChecked = sub.id in selectedSubFilterIds

                                    InputCheckbox(
                                        modifier = Modifier.clip(shape = ShapeDefaults.Medium),
                                        height = 55.dp,
                                        checked = isChecked,
                                        headLine = sub.name,
                                        onCheckedChange = {
                                            onToggleOption(filter.id, sub.id, false)
                                        }
                                    )

                                    Spacer(Modifier.height(SpacingS))
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(BasePadding))
                }
            }
        }
    }
    Spacer(Modifier.height(BasePadding))
}

