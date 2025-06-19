package com.example.scrollbooker.screens.profile.edit
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.list.ItemListInfo
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.screens.profile.myProfile.ProfileSharedViewModel
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: ProfileSharedViewModel
) {
    val userState by viewModel.userProfileState.collectAsState()
    val user = (userState as? FeatureState.Success)?.data

    Layout(
        headerTitle = stringResource(R.string.editProfile),
        onBack = onBack,
        enablePaddingH = false
    ) {
        Column(Modifier.padding(
            top = BasePadding,
            start = BasePadding,
            end = BasePadding
        )) {
            Text(
                style = titleMedium,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceBG,
                text = stringResource(R.string.aboutYou)
            )
        }
        Spacer(Modifier.padding(vertical = SpacingS))
        ItemListInfo(
            headLine = stringResource(R.string.name),
            supportingText = user?.fullName ?: "",
            onClick = { onNavigate(MainRoute.EditFullName.route) }
        )
        Spacer(Modifier.padding(vertical = SpacingXXS))
        ItemListInfo(
            headLine = stringResource(R.string.username),
            supportingText = user?.username ?: "",
            onClick = { onNavigate(MainRoute.EditUsername.route) }
        )
        Spacer(Modifier.padding(vertical = SpacingXXS))
        ItemListInfo(
            headLine = stringResource(R.string.biography),
            supportingText = user?.bio ?: "",
            onClick = { onNavigate(MainRoute.EditBio.route) }
        )
        Spacer(Modifier.padding(vertical = SpacingXXS))
        ItemListInfo(
            headLine = stringResource(R.string.gender),
            supportingText = user?.gender ?: "",
            onClick = { onNavigate(MainRoute.EditGender.route) }
        )
        ItemListInfo(
            headLine = stringResource(R.string.profession),
            supportingText = user?.profession ?: "",
            onClick = { onNavigate(MainRoute.EditProfession.route) }
        )
        Button(onClick = {  }) {
            Text("Logout")
        }
    }
}