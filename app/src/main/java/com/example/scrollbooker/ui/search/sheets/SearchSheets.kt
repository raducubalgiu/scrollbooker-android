package com.example.scrollbooker.ui.search.sheets

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSheets(
    sheetState: SheetState,
    sheetContent: SearchSheetsContent,
    onClose: () -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onDismissRequest = onClose,
        containerColor = Background,
        contentColor = OnBackground,
        dragHandle = {},
        contentWindowInsets = { BottomSheetDefaults.windowInsets },
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false
        ),
    ) {
        when(val content = sheetContent) {
            is SearchSheetsContent.ServicesSheet -> {
                SearchServicesSheet(onClose)
            }
            is SearchSheetsContent.PriceSheet -> {
                SearchPriceSheet(onClose)
            }
            is SearchSheetsContent.SortSheet -> {
                SearchSortSheet(onClose)
            }
            is SearchSheetsContent.RatingSheet -> {
                SearchRatingSheet(onClose)
            }
            is SearchSheetsContent.None -> Unit
        }
    }
}