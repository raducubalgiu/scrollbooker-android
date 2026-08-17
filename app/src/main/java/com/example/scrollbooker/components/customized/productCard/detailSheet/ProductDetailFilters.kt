package com.example.scrollbooker.components.customized.productCard.detailSheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.products.domain.model.ProductFilter
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailFilters(filters: List<ProductFilter>) {
    filters.forEach { filter ->
        Column(
            modifier = Modifier.padding(horizontal = BasePadding)
        ) {

            Text(
                text = filter.name,
                style = titleMedium,
                modifier = Modifier.padding(bottom = 6.dp),
                fontWeight = FontWeight.SemiBold
            )

            FlowRow {
                filter.subFilters.forEach { sub ->
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            sub.description?.let { desc ->
                                Text(
                                    text = desc,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        },
                        state = rememberTooltipState()
                    ) {
                        SuggestionChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = sub.name,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                )
                            },
                            shape = ShapeDefaults.ExtraLarge,
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = Color.Transparent
                            ),
                            border = SuggestionChipDefaults.suggestionChipBorder(
                                enabled = true,
                                borderColor = Divider,
                                borderWidth = 1.dp
                            ),
                            modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
                        )
                    }
                }
            }
        }
    }
}