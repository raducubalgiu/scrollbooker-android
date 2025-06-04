package com.example.scrollbooker.feature.profile.presentation.edit
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.components.list.ItemListInfo
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: ProfileSharedViewModel
) {
    Layout(
        headerTitle = stringResource(R.string.editProfile),
        onBack = onBack,
        enablePadding = false
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

        ItemListInfo(
            headLine = stringResource(R.string.name),
            supportingText = viewModel.user?.fullName ?: "",
            onClick = { onNavigate(MainRoute.EditFullName.route) }
        )
        ItemListInfo(
            headLine = stringResource(R.string.username),
            supportingText = viewModel.user?.username ?: "",
            onClick = { onNavigate(MainRoute.EditUsername.route) }
        )
        ItemListInfo(
            headLine = stringResource(R.string.biography),
            supportingText = viewModel.user?.bio ?: "",
            onClick = { onNavigate(MainRoute.EditBio.route) }
        )
        ItemListInfo(
            headLine = stringResource(R.string.gender),
            supportingText = "Male",
            onClick = { onNavigate(MainRoute.EditGender.route) }
        )
    }
}