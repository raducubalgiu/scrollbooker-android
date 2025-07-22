package com.example.scrollbooker.ui.profile.myProfile.myBusiness.myEmployees
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun EmployeesDismissalScreen(
    //viewModel: EmployeesDismissalViewModel,
    onBack: () -> Unit
) {
//    val state by viewModel.employeeDismissalState.collectAsState()
//
//    var agreed by remember { mutableStateOf(false) }
//    var scrollState = rememberScrollState()
//
//    Layout(
//        headerTitle = stringResource(R.string.dismissal),
//        onBack = onBack
//    ) {
//        Column(modifier = Modifier
//            .fillMaxSize()
//            .padding(top = BasePadding),
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            Column(modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f)
//                .background(SurfaceBG)
//                .padding(BasePadding)
//                .verticalScroll(scrollState)
//            ) {
//                when(state) {
//                    is FeatureState.Loading -> LoadingScreen()
//                    is FeatureState.Error -> ErrorScreen()
//                    is FeatureState.Success -> {
//                        val consent = (state as FeatureState.Success).data
//
//                        Text(consent.text)
//                    }
//                }
//            }
//            Column {
//                Row(modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = BasePadding),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Checkbox(
//                        checked = agreed,
//                        onCheckedChange = { agreed = it }
//                    )
//                    Text(
//                        text = stringResource(R.string.acceptTermsAndConditions),
//                        maxLines = Int.MAX_VALUE,
//                        style = bodyLarge
//                    )
//                }
//                MainButton(
//                    title = stringResource(R.string.dismiss),
//                    onClick = {},
//                    enabled = agreed && state is FeatureState.Success
//                )
//            }
//        }
//    }
}