package com.example.scrollbooker.ui.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.inputs.InputSelect
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun SearchAdvancedFilters(
    isLoading: Boolean,
    filters: List<String>,
    serviceName: String?
) {
    AnimatedVisibility(
        visible = isLoading || filters.isNotEmpty(),
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(), // să nu sară sheet-ul brusc
            shape = RoundedCornerShape(16.dp),
            color = SurfaceBG
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "Filtrare avansată",
                    style = titleMedium
                )

                Spacer(Modifier.height(BasePadding))

                when {
                    isLoading -> {
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
                    else -> {
                        filters.forEachIndexed { index, filter ->
                            InputSelect(
                                options = listOf(Option(value = index.toString(), name = filter)),
                                selectedOption = "",
                                placeholder = filter,
                                onValueChange = {},
                                isRequired = false,
                                background = Background,
                                color = OnBackground
                            )
                            //FilterInputRow(filter)
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}
