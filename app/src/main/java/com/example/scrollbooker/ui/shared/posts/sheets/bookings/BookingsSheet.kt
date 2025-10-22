package com.example.scrollbooker.ui.shared.posts.sheets.bookings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.shared.calendar.CalendarViewModel
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.components.BookingSheetHeader
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.tabs.CalendarTab
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.tabs.ConfirmTab
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.tabs.ProductsTab
import com.example.scrollbooker.ui.shared.products.UserProductsViewModel
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import kotlinx.coroutines.delay

@Composable
fun ProductsSheet(
    userId: Int,
    initialPage: Int,
    onClose: () -> Unit
) {
    val productsViewModel: UserProductsViewModel = hiltViewModel()
    val calendarViewModel: CalendarViewModel = hiltViewModel()

    val selectedProducts by productsViewModel.selectedProducts.collectAsState()

    val totalDuration = selectedProducts.sumOf { it.duration }
    val totalPrice = selectedProducts.sumOf { it.priceWithDiscount }

    val scope = rememberCoroutineScope()
    val steps = listOf(
        "Alege serviciile",
        "Alege ora",
        "Confirma programarea"
    )

    val pagerState = rememberPagerState { steps.size }
    val currentStep = pagerState.currentPage

    var isSaving by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        BookingSheetHeader(
            stepTitle = steps[currentStep],
            onClose = onClose,
            totalSteps = steps.size,
            currentStep = currentStep
        )

        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            VerticalPager(
                state = pagerState,
                beyondViewportPageCount = 0,
                userScrollEnabled = false
            ) { page ->
                when(page) {
                    0 -> ProductsTab(
                        productsViewModel = productsViewModel,
                        onSelect = { productsViewModel.toggleProductId(it) },
                        userId = userId,
                    )
                    1 -> CalendarTab(
                        calendarViewModel = calendarViewModel,
                        slotDuration = totalDuration,
                        userId = userId,
                        onSelectSlot = { scope.launch { pagerState.animateScrollToPage(page + 1) } }
                    )
                    2 -> ConfirmTab()
                }
            }
        }

        when(pagerState.currentPage) {
            0 -> {
                Column {
                    HorizontalDivider(color = Divider, thickness = 0.55.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(BasePadding),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if(selectedProducts.isEmpty()) {
                            Text(
                                text = "Nici un produs selectat",
                                style = bodyLarge,
                                color = Color.Gray
                            )
                        } else {
                            Column {
                                if(selectedProducts.isNotEmpty()) {
                                    Text(
                                        text = "$totalPrice RON",
                                        fontSize = 18.sp,
                                        style = titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(Modifier.height(SpacingXS))

                                    Text(
                                        text = "${selectedProducts.size} servicii \u2022 $totalDuration hrs",
                                        style = bodyLarge,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }

                        Button(
                            shape = ShapeDefaults.Medium,
                            contentPadding = PaddingValues(
                                vertical = BasePadding,
                                horizontal = SpacingXL
                            ),
                            enabled = totalDuration > 0,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.next),
                                style = titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
            1 -> Unit
            2 -> {
                Column {
                    HorizontalDivider(color = Divider, thickness = 0.55.dp)
                    MainButton(
                        modifier = Modifier.padding(BasePadding),
                        title = stringResource(R.string.book),
                        enabled = !isSaving,
                        isLoading = isSaving,
                        onClick = {
                            scope.launch {
                                isSaving = true
                                delay(400)
                                isSaving = false
                                onClose()
                            }
                        }
                    )
                }
            }
        }
    }
}