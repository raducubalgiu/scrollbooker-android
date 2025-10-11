package com.example.scrollbooker.ui.profile.edit
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.list.ItemListInfo
import com.example.scrollbooker.core.enums.GenderTypeEnum
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXL
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.EditProfileNavigator
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import com.example.scrollbooker.ui.theme.titleMedium

data class EditProfileAction(
    val title: String,
    val value: String,
    val navigate: () -> Unit
)

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    editProfileNavigate: EditProfileNavigator,
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
            navigate = { editProfileNavigate.toEditPublicEmail() }
        ),
        EditProfileAction(
            title = stringResource(R.string.website),
            value = user?.website ?: "",
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

    Layout(
        headerTitle = stringResource(R.string.editProfile),
        onBack = onBack,
        enablePaddingH = false
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = BasePadding),
                contentAlignment = Alignment.Center
            ) { Avatar(url = "", size = AvatarSizeXL) }

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

            actionsList.forEachIndexed { index, about ->
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