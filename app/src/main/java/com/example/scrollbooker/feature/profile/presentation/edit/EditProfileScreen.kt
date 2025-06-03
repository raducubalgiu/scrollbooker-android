package com.example.scrollbooker.feature.profile.presentation.edit
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Header
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.components.list.ItemListInfo
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: ProfileSharedViewModel
) {

    Layout(noPadding = true) {
        Header(
            modifier = Modifier.padding(horizontal = BasePadding),
            onBack = onBack,
            title = stringResource(R.string.editProfile),
        )
        Text(text = "Despre tine")

        ItemListInfo(
            headLine = stringResource(R.string.name),
            supportingText = "Radu Balgiu",
            onClick = { onNavigate(MainRoute.EditFullName.route) }
        )
        ItemListInfo(
            headLine = "Username",
            supportingText = "@radu_balgiu",
            onClick = { onNavigate(MainRoute.EditUsername.route) }
        )
        ItemListInfo(
            headLine = "Bio",
            supportingText = "Sunt medic stomatolog si sunt..",
            onClick = { onNavigate(MainRoute.EditBio.route) }
        )
        ItemListInfo(
            headLine = "Gender",
            supportingText = "Male",
            onClick = { onNavigate(MainRoute.EditGender.route) }
        )
    }
}