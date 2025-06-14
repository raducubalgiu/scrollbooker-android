package com.example.scrollbooker.feature.profile.presentation.edit
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.HeaderEdit
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.inputs.InputRadio
import com.example.scrollbooker.components.inputs.Option
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel

@Composable
fun EditGenderScreen(
    viewModel: ProfileSharedViewModel,
    onBack: () -> Unit
) {
//    val options = listOf(
//        Option(value = "male", name = "Male"),
//        Option(value = "female", name = "Female"),
//        Option(value = "other", name = "Other")
//    )
//
//    var newGender by rememberSaveable { mutableStateOf(viewModel.user?.gender ?: "") }
//    val state = viewModel.editState.collectAsState().value
//    val isLoading = state == FeatureState.Loading
//    val isEnabled = isLoading || newGender != viewModel.user?.gender
//
//    if(viewModel.isSaved) {
//        LaunchedEffect(state) {
//            onBack()
//            viewModel.isSaved = false
//        }
//    }
//
//    Layout(
//        header = {
//            HeaderEdit(
//                onBack = onBack,
//                title = stringResource(R.string.gender),
//                modifier = Modifier.padding(horizontal = BasePadding),
//                onAction = { viewModel.updateGender(newGender) },
//                actionTitle = stringResource(R.string.save),
//                isLoading = isLoading,
//                isEnabled = isEnabled
//            )
//        },
//        onBack = onBack,
//    ) {
//        InputRadio(
//            options = options,
//            value = newGender,
//            onValueChange = { newGender = it }
//        )
//    }
}