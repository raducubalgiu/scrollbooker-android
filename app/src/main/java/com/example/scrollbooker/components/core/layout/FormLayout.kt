package com.example.scrollbooker.components.core.layout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineLarge

@Composable
fun FormLayout(
    modifier: Modifier = Modifier,
    enableBottomAction: Boolean = true,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    headLine: String,
    subHeadLine: String,
    headerTitle: String? = "",
    buttonTitle: String? = "",
    onBack: (() -> Unit)? = null,
    onNext: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            Header(title = headerTitle ?: "", onBack = onBack)
        },
        bottomBar = {
            if(enableBottomAction) {
                FormLayoutBottomBar(
                    buttonTitle = buttonTitle,
                    onNext = onNext,
                    isEnabled = isEnabled,
                    isLoading = isLoading
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .then(modifier)
        ) {
            Column(Modifier.fillMaxSize()) {
                Column(Modifier.padding(horizontal = SpacingXXL)) {
                    Text(
                        style = headlineLarge,
                        color = OnBackground,
                        fontWeight = FontWeight.ExtraBold,
                        text = headLine
                    )
                    Spacer(Modifier.height(SpacingXXS))
                    Text(
                        style = bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        text = subHeadLine,
                    )
                }
                Spacer(Modifier.height(BasePadding))
                content()
            }
        }
    }
}

@Composable
private fun FormLayoutBottomBar(
    buttonTitle: String?,
    onNext: (() -> Unit)?,
    isEnabled: Boolean,
    isLoading: Boolean
) {
    HorizontalDivider(
        color = Divider,
        thickness = 0.55.dp
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = BasePadding)
    ) {
        MainButton(
            modifier = Modifier.padding(
                start = SpacingXXL,
                end = SpacingXXL,
                bottom = BasePadding
            ),
            title = buttonTitle ?: "",
            onClick = { onNext?.invoke() },
            enabled = isEnabled,
            isLoading = isLoading
        )
    }
}