package com.example.scrollbooker.ui.profile.edit
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.list.ItemListInfo
import com.example.scrollbooker.core.enums.GenderTypeEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.EditProfileNavigator
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import com.example.scrollbooker.ui.profile.edit.components.ChoosePhotoSheet
import com.example.scrollbooker.ui.profile.edit.components.EditProfileAvatar
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch

data class EditProfileAction(
    val title: String,
    val value: String,
    val hasPermission: Boolean = true,
    val navigate: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    editProfileNavigate: EditProfileNavigator,
    onNavigateToCropScreen: () -> Unit,
    viewModel: MyProfileViewModel
) {
    val userState by viewModel.userProfileState.collectAsState()
    val user = (userState as? FeatureState.Success)?.data
    val verticalScroll = rememberScrollState()

    val aboutList = listOf<EditProfileAction>(
        EditProfileAction(
            title = stringResource(R.string.name),
            value = user?.fullName ?: "",
            navigate = { editProfileNavigate.toEditFullName() }
        ),
        EditProfileAction(
            title = stringResource(R.string.username),
            value = user?.username ?: "",
            navigate = { editProfileNavigate.toEditUsername() }
        ),
        EditProfileAction(
            title = stringResource(R.string.biography),
            value = user?.bio ?: "",
            navigate = { editProfileNavigate.toEditBio() }
        ),
        EditProfileAction(
            title = stringResource(R.string.gender),
            value = GenderTypeEnum.fromKey(user?.gender)?.getLabel() ?: "",
            navigate = { editProfileNavigate.toEditGender() }
        ),
        EditProfileAction(
            title = stringResource(R.string.profession),
            value = user?.profession ?: "",
            navigate = { editProfileNavigate.toEditProfession() }
        )
    )

    val actionsList = listOf<EditProfileAction>(
        EditProfileAction(
            title = stringResource(R.string.email),
            value = user?.publicEmail ?: "",
            hasPermission = user?.isBusinessOrEmployee == true,
            navigate = { editProfileNavigate.toEditPublicEmail() }
        ),
        EditProfileAction(
            title = stringResource(R.string.website),
            value = user?.website ?: "",
            hasPermission = user?.isBusinessOrEmployee == true,
            navigate = { editProfileNavigate.toEditWebsite() }
        ),
        EditProfileAction(
            title = stringResource(R.string.instagram),
            value = user?.instagram ?: "",
            navigate = {  }
        ),
        EditProfileAction(
            title = stringResource(R.string.youtube),
            value = user?.youtube ?: "",
            navigate = {  }
        ),
        EditProfileAction(
            title = stringResource(R.string.tikTok),
            value = user?.tikTok ?: "",
            navigate = {  }
        ),
    )

    val actions = actionsList.filter { it.hasPermission }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.setPhoto(it)

            scope.launch {
                sheetState.hide()

                if (!sheetState.isVisible) {
                    onNavigateToCropScreen()
                }
            }
        }
    }

    if(sheetState.isVisible) {
        ChoosePhotoSheet(
            sheetState = sheetState,
            onPickImage = {
                scope.launch {
                    sheetState.hide()

                    if (!sheetState.isVisible) {
                        pickImage.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                }
            },
            onClose = { scope.launch { sheetState.hide() } }
        )
    }

    Layout(
        headerTitle = stringResource(R.string.editProfile),
        onBack = onBack,
        enablePaddingH = false
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll)
        ) {
            EditProfileAvatar(
                avatar = user?.avatar,
                onClick = { scope.launch { sheetState.show() } }
            )

            EditProfileSectionTitle(title = stringResource(R.string.aboutYou))

            aboutList.forEachIndexed { index, about ->
                ItemListInfo(
                    headLine = about.title,
                    supportingText = about.value,
                    onClick = about.navigate
                )

                if(index <= aboutList.size) {
                    Spacer(Modifier.padding(vertical = SpacingXXS))
                }
            }

            Spacer(Modifier.height(BasePadding))

            EditProfileSectionTitle(title = stringResource(R.string.buttonActionInProfile))

            actions.forEachIndexed { index, about ->
                ItemListInfo(
                    headLine = about.title,
                    supportingText = about.value,
                    onClick = about.navigate
                )

                if(index <= aboutList.size) {
                    Spacer(Modifier.padding(vertical = SpacingXXS))
                }
            }
        }
    }
}

@Composable
private fun EditProfileSectionTitle(title: String) {
    Column(Modifier.padding(BasePadding)) {
        Text(
            style = titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            text = title
        )
    }
}