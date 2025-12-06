package com.example.scrollbooker.ui.search.sheets

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.ui.search.SearchViewModel
import com.example.scrollbooker.ui.search.sheets.filters.SearchFiltersSheet
import com.example.scrollbooker.ui.search.sheets.services.SearchServicesSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSheets(
    viewModel: SearchViewModel,
    sheetState: SheetState,
    sheetAction: SearchSheetActionEnum,
    onClose: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Sheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onClose = onClose,
    ) {
        when(sheetAction) {
            SearchSheetActionEnum.OPEN_SERVICES -> {
                SearchServicesSheet(
                    viewModel = viewModel,
                    onClose = {
                        scope.launch {
                            sheetState.hide()
                            onClose()
                        }
                    },
                    onClear = {},
                    onFilter = {
                        scope.launch {
                            sheetState.hide()

                            if(!sheetState.isVisible) {
                                viewModel.setFiltersFromServicesSheet(
                                    businessDomainId = it.businessDomainId,
                                    businessTypeId = it.businessTypeId,
                                    serviceId = it.serviceId,
                                    subFilterIds = it.subFilterIds
                                )
                                onClose()
                            }
                        }
                    }
                )
            }
            SearchSheetActionEnum.OPEN_FILTERS -> {
                SearchFiltersSheet(
                    viewModel = viewModel,
                    onClose = {
                        scope.launch {
                            sheetState.hide()
                            onClose()
                        }
                    },
                    onFilter = {
                        scope.launch {
                            sheetState.hide()

                            if(!sheetState.isVisible) {
                                viewModel.setFiltersFromFiltersSheet(it)
                                onClose()
                            }
                        }
                    },
                )
            }
            SearchSheetActionEnum.NONE -> Unit
        }
    }
}