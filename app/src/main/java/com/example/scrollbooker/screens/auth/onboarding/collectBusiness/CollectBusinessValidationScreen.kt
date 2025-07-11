package com.example.scrollbooker.screens.auth.onboarding.collectBusiness
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineLarge
import com.example.scrollbooker.ui.theme.titleLarge

@Composable
fun CollectBusinessValidationScreen() {
    val verticalScroll = rememberScrollState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = SpacingXXL)
        .verticalScroll(verticalScroll)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(vertical = SpacingXXL),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(70.dp),
                painter = painterResource(R.drawable.ic_check_circle_outline),
                contentDescription = null,
                tint = Primary
            )
        }

        Column(Modifier.padding(horizontal = SpacingXXL)) {
            Text(
                style = headlineLarge,
                color = OnBackground,
                fontWeight = FontWeight.ExtraBold,
                text = stringResource(R.string.congratsRegisterConfirmation)
            )

            Spacer(Modifier.height(SpacingS))

            Text(
                style = bodyLarge,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                text = stringResource(R.string.pendingBusinessApprovalMessage),
            )

            Spacer(Modifier.height(SpacingXXL))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_clock_outline),
                    contentDescription = null,
                    tint = Color.Gray
                )
                Spacer(Modifier.width(SpacingS))
                Text(
                    style = bodyLarge,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    text = stringResource(R.string.usuallyWeRespondInSeveralMinutes),
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

            Spacer(Modifier.height(SpacingXXL))

            Text(
                style = bodyLarge,
                fontWeight = FontWeight.SemiBold,
                text = stringResource(R.string.dontWorryYouWillReceiveVideoGuidance)
            )
        }
    }
}