package com.example.scrollbooker.ui.search.sheets.services.steps

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.ui.search.components.SearchBusinessDomainList
import com.example.scrollbooker.ui.search.sheets.SearchSheetsHeader
import com.example.scrollbooker.ui.search.sheets.services.SearchServicesFiltersSheetState
import com.example.scrollbooker.ui.search.sheets.services.ServicesSheetStep
import com.example.scrollbooker.ui.search.sheets.services.components.MainFiltersFooter
import com.example.scrollbooker.ui.search.sheets.services.components.PopularServicesList
import com.example.scrollbooker.ui.search.sheets.services.components.ServiceDomainsList
import com.example.scrollbooker.ui.search.sheets.services.dateTimeSummary
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.headlineLarge

data class SDomain(
    val id: Int,
    val name: String,
    val url: String,
    val businessDomainId: Int
)

data class Servicee(
    val id: Int,
    val name: String,
    val url: String
)

@Composable
fun MainFiltersStep(
    state: SearchServicesFiltersSheetState,
    businessDomains: FeatureState<List<BusinessDomain>>,
    serviceDomains: FeatureState<List<ServiceDomain>>?,
    selectedBusinessDomainId: Int?,
    onSetSelectedBusinessDomainId: (Int?) -> Unit,
    onSetServiceDomainId: (Int) -> Unit,

    onOpenDate: () -> Unit,
    onFilter: (SearchServicesFiltersSheetState) -> Unit,
    onClose: () -> Unit,
    onClear: () -> Unit
) {
    val buttonSummary = state.dateTimeSummary()
    val isActive = buttonSummary != null

//    val hasDomainChanged = selectedBusinessDomainId != requestBusinessDomainId
//    val hasOtherFiltersChanged = state.hasChangesComparedTo(requestState.filters)

//    val isClearEnabled = selectedBusinessDomainId != null || state.hasActiveFilters()
//    val isConfirmEnabled = hasDomainChanged || hasOtherFiltersChanged

    val fakeSDomains = listOf(
        // Beauty
        SDomain(
            id = 1,
            name = "Par si barba",
            url = "https://images.unsplash.com/photo-1593702275687-f8b402bf1fb5?w=1200&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTN8fGJhcmJlciUyMHNob3B8ZW58MHwwfDB8fHwy",
            businessDomainId = 1
        ),
        SDomain(
            id = 2,
            name = "Sprancene si gene",
            url = "https://images.unsplash.com/photo-1613966802194-d46a163af70d?w=1200&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTV8fG1ha2V1cHxlbnwwfDB8MHx8fDI%3D",
            businessDomainId = 1
        ),
        SDomain(
            id = 3,
            name = "Unghii",
            url = "https://images.unsplash.com/photo-1632345031435-8727f6897d53?w=1200&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8bmFpbHN8ZW58MHwwfDB8fHwy",
            businessDomainId = 1
        ),
        SDomain(
            id = 4,
            name = "Cosmetica faciala",
            url = "https://images.unsplash.com/photo-1616394584738-fc6e612e71b9?w=1200&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8c2tpbiUyMGNhcmV8ZW58MHwwfDB8fHwy",
            businessDomainId = 1
        ),
        SDomain(
            id = 5,
            name = "Epilare",
            url = "https://media.istockphoto.com/id/1388628689/ro/fotografie/b%C4%83rbatul-care-prime%C8%99te-o-epilare-cu-laser-pe-piept.jpg?s=1024x1024&w=is&k=20&c=9R5FNF_WGAPgKZrOZ2ckxyxV4qEpJYbt5E_Wbv5ARh8=",
            businessDomainId = 1
        ),
        SDomain(
            id = 6,
            name = "Masaj",
            url = "https://images.unsplash.com/photo-1570174006382-148305ce4972?w=1200&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTV8fG1hc3NhZ2V8ZW58MHwwfDB8fHwy",
            businessDomainId = 1
        ),
        // Medical
        SDomain(
            id = 7,
            name = "Stomatologie",
            url = "https://images.unsplash.com/photo-1684607633138-6cc13613369b?w=1200&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjB8fHN0b21hdG9sb2d8ZW58MHwwfDB8fHwy",
            businessDomainId = 2
        ),
        SDomain(
            id = 8,
            name = "Veterinar",
            url = "https://images.unsplash.com/photo-1517451330947-7809dead78d5?w=1200&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTV8fHZldHxlbnwwfDB8MHx8fDI%3D",
            businessDomainId = 2
        ),
        SDomain(
            id = 9,
            name = "Psihologie",
            url = "https://images.unsplash.com/photo-1573497491208-6b1acb260507?w=1200&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dGhlcmFweXxlbnwwfDB8MHx8fDI%3D",
            businessDomainId = 2
        ),
        // Auto
        SDomain(
            id = 10,
            name = "ITP",
            url = "https://images.unsplash.com/photo-1652987086659-2662433aed85?w=1200&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8YXV0byUyMHNlcnZpY2V8ZW58MHwwfDB8fHwy",
            businessDomainId = 3
        ),
        SDomain(
            id = 11,
            name = "Detailing auto",
            url = "https://images.unsplash.com/photo-1753080139174-0ecb6293cc3b?w=1200&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8ZGV0YWlsaW5nJTIwYXV0b3xlbnwwfDB8MHx8fDI%3D",
            businessDomainId = 3
        ),
    )



    Column(Modifier.fillMaxSize()) {
        SearchSheetsHeader(
            title = "",
            onClose = onClose
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier.padding(horizontal = BasePadding),
                style = headlineLarge,
                color = OnBackground,
                fontWeight = FontWeight.ExtraBold,
                text = stringResource(R.string.services)
            )

            Spacer(Modifier.height(BasePadding))

            Column(modifier = Modifier.fillMaxSize()) {
                SearchBusinessDomainList(
                    businessDomains = businessDomains,
                    selectedBusinessDomain = selectedBusinessDomainId,
                    shadowElevation = 0.dp,
                    inactiveContainerColor = SurfaceBG,
                    inactiveContentColor = OnSurfaceBG,
                    paddingValues = PaddingValues(
                        horizontal = 26.dp,
                        vertical = 12.dp
                    ),
                    shape = ShapeDefaults.Medium,
                    onClick = { onSetSelectedBusinessDomainId(it) }
                )

                AnimatedContent(
                    targetState = selectedBusinessDomainId,
                    transitionSpec = {
                        if (targetState == null) {
                            fadeIn() togetherWith fadeOut()
                        } else {
                            fadeIn() togetherWith fadeOut()
                        }.using(
                            SizeTransform(clip = false)
                        )
                    }
                ) { currentStep ->
                    if(currentStep == null) {
                        PopularServicesList()
                    } else {
                        ServiceDomainsList(
                            serviceDomains = serviceDomains,
                            onSetServiceDomainId = onSetServiceDomainId
                        )
                    }
                }
            }
        }

        MainFiltersFooter(
            isClearEnabled = true,
            isConfirmEnabled = true,
            onConfirm = { onFilter(state) },
            onClear = onClear,
            onOpenDate = onOpenDate,
            summary = buttonSummary,
            isActive = isActive
        )
    }
}