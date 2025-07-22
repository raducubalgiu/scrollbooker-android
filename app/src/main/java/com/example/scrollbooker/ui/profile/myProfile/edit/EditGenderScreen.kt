package com.example.scrollbooker.ui.profile.myProfile.edit
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.HeaderEdit
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.core.enums.GenderTypeEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.profile.myProfile.ProfileSharedViewModel
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun EditGenderScreen(
    viewModel: ProfileSharedViewModel,
    onBack: () -> Unit
) {
    val userState by viewModel.userProfileState.collectAsState()
    val user = (userState as? FeatureState.Success)?.data

    val genders = listOf(
        GenderTypeEnum.MALE,
        GenderTypeEnum.FEMALE,
        GenderTypeEnum.OTHER
    )

    var newGender by rememberSaveable { mutableStateOf(user?.gender ?: "") }

    val state = viewModel.editState.collectAsState().value
    val isLoading = state == FeatureState.Loading
    val isEnabled = isLoading || newGender != user?.gender

    if(viewModel.isSaved) {
        LaunchedEffect(state) {
            onBack()
            viewModel.isSaved = false
        }
    }

    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        HeaderEdit(
            onBack = onBack,
            title = stringResource(R.string.gender),
            modifier = Modifier.padding(horizontal = BasePadding),
            onAction = { viewModel.updateGender(newGender) },
            actionTitle = stringResource(R.string.save),
            isLoading = isLoading,
            isEnabled = isEnabled
        )

        LazyColumn {
            itemsIndexed(genders) { index, gender ->
                InputRadio(
                    selected = gender.key == newGender,
                    onSelect = { newGender = gender.key },
                    headLine = gender.getLabel()
                )

                if(index < genders.size) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = SpacingXXL)
                            .height(0.55.dp)
                            .background(Divider.copy(alpha = 0.5f))
                    )
                }
            }
        }
    }
}