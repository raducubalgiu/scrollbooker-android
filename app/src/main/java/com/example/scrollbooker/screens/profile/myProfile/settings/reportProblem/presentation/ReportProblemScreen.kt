package com.example.scrollbooker.screens.profile.myProfile.settings.reportProblem.presentation
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.HeaderEdit
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.inputs.EditInput
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun ReportProblemScreen(
    viewModel: ReportAProblemViewModel,
    onBack: () -> Unit
) {
    val sendState by viewModel.reportAProblemState.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }

    Layout(
        modifier = Modifier.statusBarsPadding(),
        onBack = onBack,
        header = {
            HeaderEdit(
                modifier = Modifier.padding(horizontal = BasePadding),
                title = stringResource(R.string.report),
                actionTitle = stringResource(R.string.save),
                onBack = onBack,
                onAction = { viewModel.sendProblem(text) },
                isLoading = (sendState) is FeatureState.Loading,
                isEnabled = text.length > 20 && text.length < 500
            )
        }
    ) {
        EditInput(
            value = text,
            onValueChange = { text = it },
            placeholder = stringResource(R.string.reportProblem),
            singleLine = false,
            minLines = 5,
            maxLines = 5,
            isError = sendState is FeatureState.Error,
            isEnabled = sendState != FeatureState.Loading
        )
        Spacer(Modifier.height(BasePadding))
        Text(
            text = stringResource(R.string.tellUsWhatWentWrong),
            style = bodyMedium,
            color = OnSurfaceBG
        )
        Spacer(Modifier.height(BasePadding))
        Text(
            text = stringResource(R.string.allMessagesAreComingToOurTeam),
            style = bodyMedium,
            color = OnSurfaceBG
        )
        Spacer(Modifier.height(BasePadding))
        Text(
            text = stringResource(R.string.pleaseGiveUseAllTheDetails),
            style = bodyMedium,
            color = OnSurfaceBG
        )
        Spacer(Modifier.height(SpacingXXL))
        Text(
            text = stringResource(R.string.thankYouForHelp),
            style = bodyMedium,
            color = OnSurfaceBG
        )
    }
}