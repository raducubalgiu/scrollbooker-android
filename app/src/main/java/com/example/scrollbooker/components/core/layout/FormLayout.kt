package com.example.scrollbooker.components.core.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineLarge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormLayout(
    enableBack: Boolean = true,
    isEnabled: Boolean = true,
    headerTitle: String = "",
    headLine: String,
    subHeadLine: String,
    buttonTitle: String,
    onBack: () -> Unit,
    onNext: () -> Unit,
    content: @Composable () -> Unit,
) {
    Layout(
        headerTitle = headerTitle,
        onBack = onBack,
        enablePaddingH = false,
        enableBack = enableBack
    ) {
        Column(Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
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
            Column(Modifier.fillMaxWidth()) {
                MainButton(
                    modifier = Modifier.padding(
                        vertical = BasePadding,
                        horizontal = SpacingXXL
                    ),
                    title = buttonTitle,
                    onClick = onNext,
                    enabled = isEnabled
                )
            }
        }
    }
}