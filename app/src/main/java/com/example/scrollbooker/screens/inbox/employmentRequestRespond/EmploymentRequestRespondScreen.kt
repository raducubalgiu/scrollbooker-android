package com.example.scrollbooker.screens.inbox.employmentRequestRespond
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
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
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium
import androidx.compose.runtime.getValue
import com.example.scrollbooker.core.util.FeatureState

@Composable
fun EmploymentRequestRespondScreen(
    viewModel: EmploymentRequestRespondViewModel,
    onBack: () -> Unit,
    onNavigate: () -> Unit
) {
    val isSaving by viewModel.isSaving.collectAsState()

    val verticalScroll = rememberScrollState()

    val paragraphs = listOf(
        stringResource(R.string.youWillReceiveAccessToYourOwnCalendarAndAppointments),
        "${stringResource(R.string.youWillBeAbleToEditAndAddServicesWithinYourBusiness)} Frizeria Figaro",
        stringResource(R.string.clientsWillBeAbleToSelectYouDirectlyBasedYourAvailability),
        stringResource(R.string.youWillAppearInThePublicBusinessProfile),
        stringResource(R.string.youWillReceiveReviewsFromYourClients),
        stringResource(R.string.youCouldResignAnytime)
    )

    Layout(
        modifier = Modifier
            .background(Background)
            .statusBarsPadding(),
        enablePaddingH = false,
        onBack = onBack
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(horizontal = SpacingXXL)
                .verticalScroll(verticalScroll)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
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
                                append("Frizeria Figaro ")
                            }
                            append(stringResource(R.string.sentYouAnEmploymentRequest))
                        },
                        style = bodyLarge
                    )
                }

                Spacer(Modifier.height(SpacingXXL))

                Text(
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    text = stringResource(R.string.hereIsWhatYouShouldNow)
                )
                Spacer(Modifier.height(SpacingS))

                Text(
                    style = bodyLarge,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    text = "${stringResource(R.string.byAcceptingThisRequest)}:"
                )

                paragraphs.forEach { text ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = BasePadding),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(
                                    top = 6.dp,
                                    start = BasePadding
                                )
                                .clip(CircleShape)
                                .size(5.dp)
                                .background(OnBackground)
                                .alignBy(FirstBaseline)
                        )
                        Spacer(Modifier.width(SpacingS))
                        Text(
                            style = bodyLarge,
                            color = Color.Gray,
                            text = text
                        )
                    }
                }

                Spacer(Modifier.height(BasePadding))
            }
            Column {
                HorizontalDivider(color = Divider, thickness = 0.5.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = BasePadding)
                        .padding(top = SpacingM, bottom = SpacingM)
                ) {
                    MainButton(
                        modifier = Modifier.weight(0.5f),
                        title = stringResource(R.string.deny),
                        isLoading = isSaving is FeatureState.Loading,
                        enabled = isSaving != FeatureState.Loading,
                        onClick = {  },
                        colors = ButtonColors(
                            containerColor = SurfaceBG,
                            contentColor = OnSurfaceBG,
                            disabledContainerColor = Divider,
                            disabledContentColor = OnSurfaceBG
                        ),
                    )

                    Spacer(Modifier.width(SpacingS))
                    MainButton(
                        modifier = Modifier.weight(0.5f),
                        title = stringResource(R.string.accept),
                        isLoading = isSaving is FeatureState.Loading,
                        enabled = isSaving != FeatureState.Loading,
                        onClick = onNavigate,
                    )
                }
            }
        }
    }
}