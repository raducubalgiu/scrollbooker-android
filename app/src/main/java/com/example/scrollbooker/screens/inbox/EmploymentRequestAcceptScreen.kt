package com.example.scrollbooker.screens.inbox
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun EmploymentRequestAcceptScreen(
    onBack: () -> Unit
) {
    Layout(
        modifier = Modifier
            .background(Background)
            .statusBarsPadding(),
        enablePaddingH = false,
        onBack = onBack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SpacingXXL),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(70.dp),
                        painter = painterResource(R.drawable.ic_clipboard_check_outline),
                        contentDescription = null,
                        tint = Primary
                    )
                    Spacer(Modifier.height(SpacingXL))
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                                append("Frizeria Figaro")
                            }
                            append(" ți-a trimis o cerere de angajare")
                        },
                        style = bodyLarge
                    )
                }

                Spacer(Modifier.height(SpacingXXL))

                Text(
                    style = titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    text = stringResource(R.string.hereIsWhatYouShouldNow)
                )
                Spacer(Modifier.height(SpacingS))

                Text(
                    style = bodyLarge,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    text = stringResource(R.string.afterValidationYouNeedToAddEmployees)
                )

                Spacer(Modifier.height(BasePadding))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(start = BasePadding),
                        style = bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        text = "\u2022")
                    Spacer(Modifier.width(SpacingS))
                    Text(
                        style = bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        text = stringResource(R.string.employeeShouldCreateAccount)
                    )
                }

                Spacer(Modifier.height(BasePadding))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(start = BasePadding),
                        style = bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        text = "\u2022")
                    Spacer(Modifier.width(SpacingS))
                    Text(
                        style = bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        text = stringResource(R.string.afterEmployeeCreateAccountYouSendEmploymentRequest)
                    )
                }

                Spacer(Modifier.height(BasePadding))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(start = BasePadding),
                        style = bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        text = "\u2022")
                    Spacer(Modifier.width(SpacingS))
                    Text(
                        style = bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray,
                        text = stringResource(R.string.employeeWillReceiveEmploymentRequest)
                    )
                }
            }
            Row(
                modifier = Modifier.padding(bottom = BasePadding)
            ) {
                MainButton(
                    title = "Anuleaza",
                    onClick = {},
                    shape = ShapeDefaults.Small,
                    colors = ButtonColors(
                        containerColor = SurfaceBG,
                        contentColor = OnSurfaceBG,
                        disabledContainerColor = Divider,
                        disabledContentColor = OnSurfaceBG
                    ),
                    modifier = Modifier
                        .weight(0.5f)
                )

                Spacer(Modifier.width(SpacingM))
                MainButton(
                    modifier = Modifier
                        .weight(0.5f),
                    title = "Accepta",
                    onClick = {},
                    shape = ShapeDefaults.Small
                )
            }
        }
    }
}