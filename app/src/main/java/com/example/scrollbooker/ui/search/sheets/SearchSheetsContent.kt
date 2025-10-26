package com.example.scrollbooker.ui.search.sheets

sealed class SearchSheetsContent {
    data class ServicesSheet(val userId: Int): SearchSheetsContent()
    data class PriceSheet(val userId: Int): SearchSheetsContent()
    data class SortSheet(val userId: Int): SearchSheetsContent()
    data class RatingSheet(val userId: Int): SearchSheetsContent()
    object None: SearchSheetsContent()
}