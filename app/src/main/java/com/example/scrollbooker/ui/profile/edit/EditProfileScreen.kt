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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.list.ItemListInfo
import com.example.scrollbooker.core.enums.GenderTypeEnum
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.core.util.AppLocaleProvider
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.LocalUserPermissions
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import com.example.scrollbooker.ui.profile.edit.components.ChoosePhotoSheet
import com.example.scrollbooker.ui.profile.edit.components.EditProfileAvatar
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch

data class EditProfileAction(
    val title: String,
    val value: String,
    val permission: PermissionEnum = PermissionEnum.NO_PROTECTION,
    val extraPermission: Boolean = true,
    val navigate: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    profileNavigate: ProfileNavigator,
    onNavigateToCropScreen: () -> Unit,
    viewModel: MyProfileViewModel
) {
    val userState by viewModel.profile.collectAsState()
    val user = (userState as? FeatureState.Success)?.data
    val verticalScroll = rememberScrollState()

    val permissionController = LocalUserPermissions.current

    val formattedBirthdate = remember(user?.dateOfBirth) {
        val rawDate = user?.dateOfBirth
        if (!rawDate.isNullOrBlank()) {
            try {
                val parsedDate = org.threeten.bp.LocalDate.parse(rawDate)
                val currentLocale = AppLocaleProvider.current()
                val formatter = org.threeten.bp.format.DateTimeFormatter.ofPattern("d MMMM yyyy", currentLocale)
                parsedDate.format(formatter)
            } catch (_: Exception) {
                rawDate
            }
        } else {
            ""
        }
    }

    val aboutListRaw = listOf<EditProfileAction>(
        EditProfileAction(
            title = stringResource(R.string.name),
            value = user?.fullName ?: "",
            navigate = { profileNavigate.toEditFullName() }
        ),
        EditProfileAction(
            title = stringResource(R.string.username),
            value = user?.username ?: "",
            navigate = { profileNavigate.toEditUsername() }
        ),
        EditProfileAction(
            title = stringResource(R.string.biography),
            value = user?.bio ?: "",
            navigate = { profileNavigate.toEditBio() }
        ),
        EditProfileAction(
            title = stringResource(R.string.gender),
            value = GenderTypeEnum.fromKey(user?.gender)?.getLabel() ?: "",
            navigate = { profileNavigate.toEditGender() },
            permission = PermissionEnum.GENDER_EDIT
        ),
        EditProfileAction(
            title = stringResource(R.string.dateOfBirth),
            value = formattedBirthdate,
            navigate = { profileNavigate.toEditBirthdate() },
            permission = PermissionEnum.BIRTHDATE_EDIT
        ),
    )

    val aboutList = remember(aboutListRaw, permissionController.values) {
        aboutListRaw.filter { action ->
            val hasSystemPermission = action.permission == PermissionEnum.NO_PROTECTION ||
                    permissionController.hasPermission(action.permission)

            hasSystemPermission && action.extraPermission
        }
    }

// V2 - Will be implemented in next iterations, for now it's hidden in the code
//    val actionsListRaw = listOf<EditProfileAction>(
//        EditProfileAction(
//            title = stringResource(R.string.email),
//            value = user?.publicEmail ?: "",
//            permission = PermissionEnum.NO_PROTECTION,
//            extraPermission = user?.isBusinessOrEmployee == true,
//            navigate = { editProfileNavigate.toEditPublicEmail() }
//        ),
//        EditProfileAction(
//            title = stringResource(R.string.website),
//            value = user?.website ?: "",
//            extraPermission = user?.isBusinessOrEmployee == true,
//            navigate = { editProfileNavigate.toEditWebsite() }
//        ),
//        EditProfileAction(
//            title = stringResource(R.string.instagram),
//            value = user?.instagram ?: "",
//            navigate = {  }
//        ),
//        EditProfileAction(
//            title = stringResource(R.string.youtube),
//            value = user?.youtube ?: "",
//            navigate = {  }
//        ),
//        EditProfileAction(
//            title = stringResource(R.string.tikTok),
//            value = user?.tikTok ?: "",
//            navigate = {  }
//        ),
//    )
//
//    val actions = remember(actionsListRaw, permissionController.values) {
//        actionsListRaw.filter { action ->
//            val hasSystemPermission = action.permission == PermissionEnum.NO_PROTECTION ||
//                    permissionController.hasPermission(action.permission)
//
//            hasSystemPermission && action.extraPermission
//        }
//    }

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

            if (aboutList.isNotEmpty()) {
                EditProfileSectionTitle(title = stringResource(R.string.aboutYou))

                aboutList.forEachIndexed { index, about ->
                    ItemListInfo(
                        headLine = about.title,
                        supportingText = about.value,
                        onClick = about.navigate
                    )

                    if(index < aboutList.size - 1) {
                        Spacer(Modifier.padding(vertical = SpacingXXS))
                    }
                }
            }

            Spacer(Modifier.height(BasePadding))

//            if (actions.isNotEmpty()) {
//                EditProfileSectionTitle(title = stringResource(R.string.buttonActionInProfile))
//
//                actions.forEachIndexed { index, about ->
//                    ItemListInfo(
//                        headLine = about.title,
//                        supportingText = about.value,
//                        onClick = about.navigate
//                    )
//
//                    if(index < actions.size - 1) {
//                        Spacer(Modifier.padding(vertical = SpacingXXS))
//                    }
//                }
//            }
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