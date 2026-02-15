package com.example.scrollbooker.components.customized.Picker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.TextStyle
import java.util.Locale

data class PickerState(
    val selectedDate: LocalDate? = null
)

@Composable
fun Picker(
    locale: Locale,
    state: PickerState,
    monthsForwardLimit: Int = 6,
    onDateSelected: (LocalDate) -> Unit
) {
    val scope = rememberCoroutineScope()

    val today = LocalDate.now()
    val currentMonth = YearMonth.now()

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { monthsForwardLimit + 1 }
    )

    val visibleMonth = currentMonth.plusMonths(
        pagerState.currentPage.toLong()
    )

    val isBackEnabled = pagerState.currentPage > 0
    val isNextEnabled = pagerState.currentPage < monthsForwardLimit

    Column(modifier = Modifier.fillMaxWidth()) {
        PickerHeader(
            title = visibleMonth.month.getDisplayName(
                TextStyle.FULL,
                locale
            ).replaceFirstChar { it.uppercase() } +
                    " ${visibleMonth.year}",
            isBackEnabled = isBackEnabled,
            isNextEnabled = isNextEnabled,
            onBack = {
                scope.launch {
                    pagerState.animateScrollToPage(
                        pagerState.currentPage - 1
                    )
                }
            },
            onNext = {
                scope.launch {
                    pagerState.animateScrollToPage(
                        pagerState.currentPage + 1
                    )
                }
            }
        )

        PickerWeekHeader(
            locale = locale,
            modifier = Modifier.padding(horizontal = BasePadding)
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1
        ) { page ->

            val month = currentMonth.plusMonths(page.toLong())

            PickerMonthGrid(
                month = month,
                locale = locale,
                today = today,
                selectedDate = state.selectedDate,
                onDateSelected = onDateSelected
            )
        }
    }
}