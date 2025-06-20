package com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.collectBusinessType
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.components.core.inputs.Option
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.CollectBusinessDetails
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge

data class BusinessType(
    val id: Int,
    val name: String
)

@Composable
fun CollectBusinessTypeScreen(
    viewModel: CollectBusinessTypeViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    CollectBusinessDetails(
        isFirstScreen = false,
        headLine = stringResource(id = R.string.collectBusinessTypeHeadline),
        subHeadLine = stringResource(id = R.string.collectBusinessTypeSubHeadline),
        onBack = onBack,
        onNext = onNext,
    ) {
        val businessTypes = listOf(
            BusinessType(id = 1, name = "Frizerie"),
            BusinessType(id = 2, name = "Salon de infrumusetare"),
            BusinessType(id = 3, name = "Cabinet medical"),
            BusinessType(id = 4, name = "Restaurant"),
            BusinessType(id = 5, name = "Statie ITP"),
            BusinessType(id = 5, name = "Statie ITP"),
            BusinessType(id = 5, name = "Statie ITP"),
            BusinessType(id = 5, name = "Statie ITP"),
            BusinessType(id = 5, name = "Statie ITP"),
            BusinessType(id = 5, name = "Statie ITP"),
            BusinessType(id = 5, name = "Statie ITP"),
            BusinessType(id = 5, name = "Statie ITP")
        )

        LazyColumn {
            itemsIndexed(businessTypes) { index, item ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(Background)
                        .selectable(
                            selected = false,
                            onClick = {  },
                            role = Role.RadioButton
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(start = SpacingXXL),
                        text = item.name,
                        style = bodyLarge,
                        color = OnBackground
                    )
                    RadioButton(
                        modifier = Modifier.scale(1.3f).padding(end = SpacingXXL),
                        selected = false,
                        onClick = null,
                        colors = RadioButtonColors(
                            selectedColor = Primary,
                            unselectedColor = Divider,
                            disabledSelectedColor = Divider,
                            disabledUnselectedColor = Divider
                        )
                    )
                }

                if(index < businessTypes.lastIndex) {
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