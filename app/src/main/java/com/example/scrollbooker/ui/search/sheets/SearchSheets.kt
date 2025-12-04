package com.example.scrollbooker.ui.search.sheets

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.sheets.filters.SearchFiltersSheet
import com.example.scrollbooker.ui.search.sheets.services.SearchServicesSheet

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
            SearchSheetActionEnum.OPEN_SERVICES -> {
                SearchServicesSheet(
                    viewModel = viewModel,
                    onClose = onClose,
                    onClear = {},
                    onFilter = {
                        viewModel.setFiltersFromServicesSheet(
                            newBusinessDomain = it.businessDomainId,
                            newServiceId = it.serviceId
                        )
                        onClose()
                    }
                )
            }
            SearchSheetActionEnum.OPEN_FILTERS -> {
                SearchFiltersSheet(
                    viewModel = viewModel,
                    onClose = onClose,
                    onFilter = { maxPrice, sort, hasDiscount, isLastMinute, hasVideo ->
                        viewModel.setFiltersFromFiltersSheet(maxPrice, sort, hasDiscount, isLastMinute, hasVideo)
                        onClose()
                    },
                )
            }
            SearchSheetActionEnum.NONE -> Unit
        }
    }
}