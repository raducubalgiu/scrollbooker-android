package com.example.scrollbooker.ui.myBusiness.unapprovedBusinesses

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.components.customized.Refresh
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.business.domain.model.UnapprovedBusiness

@Composable
fun UnapprovedBusinessesList(
    unapprovedBusinesses: LazyPagingItems<UnapprovedBusiness>,
    onRefresh: () -> Unit,
    onApprove: (Int) -> Unit,
    isSaving: FeatureState<Unit>?
) {
    val appendState = unapprovedBusinesses.loadState.append

    Refresh(
        isRefreshing = unapprovedBusinesses.loadState.refresh is LoadState.Loading,
        onRefresh = onRefresh
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = BasePadding
            )
        ) {
            items(
                count = unapprovedBusinesses.itemCount,
                key = { index -> unapprovedBusinesses[index]?.id ?: index }
            ) { index ->
                val unapprovedBusiness = unapprovedBusinesses[index]

                UnapprovedBusinessCard(
                    businessItem = unapprovedBusiness!!,
                    onApprove = onApprove,
                    onReject = { userId -> },
                    isSaving = isSaving
                )

                if(unapprovedBusinesses.itemCount > 1 && index < unapprovedBusinesses.itemCount - 1) {
                    Spacer(Modifier.height(SpacingM))
                }
            }

            if (appendState is LoadState.Loading) {
                item {
                    LoadMoreSpinner()
                }
            }
        }
    }
}