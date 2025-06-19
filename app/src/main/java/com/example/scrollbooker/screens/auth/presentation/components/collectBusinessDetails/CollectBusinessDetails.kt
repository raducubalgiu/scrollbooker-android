package com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.headlineLarge
import com.example.scrollbooker.ui.theme.titleSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectBusinessDetails(
    isFirstScreen: Boolean = false,
    isLastScreen: Boolean = false,
    headLine: String,
    subHeadLine: String,
    onBack: () -> Unit,
    onNext: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(BasePadding)
    ) {
        TopAppBar(
            title = {},
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            navigationIcon = {
                if(!isFirstScreen) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clickable(
                                onClick = onBack,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_prev),
                            contentDescription = null,
                        )
                    }
                }
            }
        )
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    style = headlineLarge,
                    color = OnBackground,
                    fontWeight = FontWeight.ExtraBold,
                    text = headLine
                )
                Text(
                    style = titleSmall,
                    fontWeight = FontWeight.Normal,
                    color = OnSurfaceBG.copy(alpha = 0.7f),
                    text = subHeadLine,
                )
                Spacer(Modifier.height(BasePadding))
                content()
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = onNext) {
                val text = if (!isLastScreen) stringResource(R.string.nextStep) else "Finish"
                Text(text = text)
            }
        }
    }
}