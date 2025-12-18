package com.example.scrollbooker.ui.camera.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun CreatePostBottomBar(
    onCreate: () -> Unit
) {
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Column {
        HorizontalDivider(color = Divider, thickness = 0.55.dp)
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = BasePadding)
            .padding(
                top = BasePadding,
                bottom = bottomInset + BasePadding
            )
        ) {
            MainButton(
                modifier = Modifier.weight(1f),
                title = "Draft",
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = SurfaceBG,
                    contentColor = OnSurfaceBG
                )
            )
            Spacer(Modifier.width(SpacingS))
            MainButton(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.postNow),
                onClick = onCreate
            )
        }
    }
}
