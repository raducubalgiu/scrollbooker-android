package com.example.scrollbooker.ui.search.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun SearchAdvancedFilters(
    selectedFilters: Map<Int, Int>,
    onSetSelectedFilter: (Int, Int) -> Unit,
    filters: List<Filter>
) {
    AnimatedVisibility(
        visible = filters.isNotEmpty(),
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
                    text = "Filtrare avansată",
                    style = titleMedium
                )

                Spacer(Modifier.height(BasePadding))

                filters.forEachIndexed { index, filter ->
                    val options = filter.subFilters.map { Option(value = it.id.toString(), name = it.name) }

                    val selectedSubFilterId = selectedFilters[filter.id]
                    val selectedOption = filter.subFilters.find { it.id == selectedSubFilterId}

                    InputSelect(
                        options = options,
                        selectedOption = selectedOption?.id?.toString() ?: "",
                        placeholder = filter.name,
                        onValueChange = { subId -> subId?.toIntOrNull()?.let { onSetSelectedFilter(filter.id, it) } },
                        isRequired = false,
                        background = Background,
                        color = OnBackground
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}
