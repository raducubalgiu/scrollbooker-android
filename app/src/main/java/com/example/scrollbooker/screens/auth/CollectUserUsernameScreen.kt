package com.example.scrollbooker.screens.auth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun CollectUserUsernameScreen() {
    FormLayout(
        enableBack = false,
        enableBottomAction = false,
        headerTitle = "",
        headLine = stringResource(R.string.usernameTitle),
        subHeadLine = stringResource(R.string.chooseUsernameDescription)
    ) {
        var username by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SpacingXL)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                shape = ShapeDefaults.Large,
                value = username,
                onValueChange = { username = it },
                label = null,
                placeholder = {
                    Text(
                        text = stringResource(R.string.usernameTitle),
                        color = Divider
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AlternateEmail,
                        contentDescription = null
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SurfaceBG,
                    unfocusedContainerColor = SurfaceBG,
                    cursorColor = Primary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Primary,
                    unfocusedLabelColor = OnSurfaceBG.copy(alpha = 0.7f),
                    focusedTextColor = OnSurfaceBG,
                    unfocusedTextColor = OnSurfaceBG
                ),
                trailingIcon = {
//                    CircularProgressIndicator(
//                        color = Divider,
//                        modifier = Modifier.size(20.dp)
//                    )
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Green
                    )
                }
            )

            Spacer(Modifier.height(BasePadding))

            MainButton(
                enabled = username.isNotEmpty(),
                onClick = {},
                title = "Salveaza",
            )
        }
    }
}