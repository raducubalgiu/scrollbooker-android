package com.example.scrollbooker.ui.search.sheets.services.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.search.domain.model.RecentSearch
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.headlineSmall

@Composable
fun RecentSearchesList(
    recentSearchesList: List<RecentSearch>
) {
    if(recentSearchesList.isEmpty()) {
        EmptyScreen(
            icon = painterResource(R.drawable.ic_search),
            message = stringResource(R.string.notFoundRecentSearches)
        )
    } else {
        LazyVerticalGrid(
            contentPadding = PaddingValues(bottom = BasePadding),
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(BasePadding)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    modifier = Modifier.padding(BasePadding),
                    style = headlineSmall,
                    color = OnBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    text = stringResource(R.string.recentSearches)
                )
            }

            items(
                recentSearchesList,
                span = { GridItemSpan(maxLineSpan) }
            ) { search ->
                RecentSearchItem(search)
            }
        }
    }
}