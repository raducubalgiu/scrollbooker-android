package com.example.scrollbooker.ui.profile.components.myProfile
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.list.ItemList
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun MyProfileMenuList(
    onNavigateToMyBusiness: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToCreatePost: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = BasePadding)
    ) {
        ItemList(
            headLine = "Creaza o postare",
            leftIcon = painterResource(R.drawable.ic_camera_outline),
            displayRightIcon = false,
            onClick = onNavigateToCreatePost
        )
        ItemList(
            headLine = stringResource(id = R.string.myBusiness),
            leftIcon = painterResource(R.drawable.ic_business_outline),
            displayRightIcon = false,
            onClick = onNavigateToMyBusiness
        )
        ItemList(
            headLine = stringResource(id = R.string.settings),
            leftIcon = painterResource(R.drawable.ic_settings_outline),
            displayRightIcon = false,
            onClick = onNavigateToSettings
        )
    }
}