package com.example.scrollbooker.feature.search.presentation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.scrollbooker.components.Layout
import com.example.scrollbooker.components.inputs.SearchBar

@Composable
fun SearchScreen() {
    var search by remember { mutableStateOf("") }

    Layout {
        SearchBar(
            value = search,
            onValueChange = { search = it },
            placeholder = "Ce serviciu cauti?",
            onSearch = {}
        )
    }
}