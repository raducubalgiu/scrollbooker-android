package com.example.scrollbooker.ui.onboarding.business
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.util.Dimens.BasePadding
import androidx.compose.runtime.getValue

@Composable
fun CollectBusinessGalleryPreviewScreen(
    viewModel: CollectBusinessViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val selectedAddress by viewModel.selectedBusinessAddress.collectAsState()

    Scaffold(
        topBar = { Header(onBack = onBack, title = stringResource(R.string.preview)) }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding)) {
            Column(Modifier.weight(1f).padding(BasePadding)) {
//                SearchCard(
//                    business = BusinessSheet(
//                        owner = TODO(),
//                        businessShortDomain = TODO(),
//                        address = selectedAddress,
//                        coordinates = TODO(),
//                        hasVideo = TODO(),
//                        mediaPreview = TODO(),
//                        products = TODO()
//                    ),
//                    onNavigateToBusinessProfile = {},
//                    onOpenBookingsSheet = {}
//                )
            }

            MainButton(
                onClick = onNext,
                title = stringResource(R.string.nextStep)
            )
        }
    }
}