package com.example.scrollbooker.ui.search.sheets

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.ui.search.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSheets(
    viewModel: SearchViewModel,
    sheetState: SheetState,
    sheetAction: SearchSheetActionEnum,
    onClose: () -> Unit
) {
    Sheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onClose = onClose,
    ) {
        when(sheetAction) {
            SearchSheetActionEnum.OPEN_SERVICES -> SearchServicesSheet(viewModel, onClose)
            SearchSheetActionEnum.OPEN_PRICE -> SearchPriceSheet(viewModel, onClose)
            SearchSheetActionEnum.OPEN_SORT -> SearchSortSheet(viewModel, onClose)
            SearchSheetActionEnum.OPEN_DISTANCE -> SearchDistanceSheet(viewModel, onClose)
            SearchSheetActionEnum.OPEN_RATINGS -> SearchRatingSheet(viewModel, onClose)
            SearchSheetActionEnum.NONE -> Unit
        }
    }
}