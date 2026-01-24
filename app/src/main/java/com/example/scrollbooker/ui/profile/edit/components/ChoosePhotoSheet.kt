package com.example.scrollbooker.ui.profile.edit.components
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.list.ItemList
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.core.util.Dimens.BasePadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoosePhotoSheet(
    sheetState: SheetState,
    onPickImage: () -> Unit,
    onClose: () -> Unit
) {
    Sheet(
        onClose = onClose,
        sheetState = sheetState
    ) {
        Column(Modifier.padding(vertical = BasePadding)) {
            ItemList(
                headLine = stringResource(id = R.string.chooseFromGallery),
                leftIcon = painterResource(R.drawable.ic_settings_outline),
                displayRightIcon = false,
                onClick = onPickImage
            )
            ItemList(
                headLine = stringResource(id = R.string.photoShoot),
                leftIcon = painterResource(R.drawable.ic_camera_outline),
                displayRightIcon = false,
                onClick = {}
            )
        }
    }
}