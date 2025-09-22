package com.example.scrollbooker.ui.shared.userCalendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.layout.Layout
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.divider.VerticalDivider
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.modules.calendar.CalendarViewModel
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun AppointmentConfirmationScreen(
    viewModel: CalendarViewModel,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
    val productState by viewModel.product.collectAsState()

    Layout(
        modifier = Modifier.safeDrawingPadding(),
        headerTitle = "",
        onBack = onBack,
        enablePaddingH = false
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                when(productState) {
                    is FeatureState.Error -> ErrorScreen()
                    is FeatureState.Loading -> LoadingScreen()
                    is FeatureState.Success -> {
                        val product = (productState as FeatureState.Success).data

                        Column(modifier = Modifier
                            .weight(1f)
                            .padding(BasePadding)
                        ) {
                            Text(
                                text = stringResource(R.string.confirmReservation),
                                style = headlineMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(Modifier.height(SpacingXL))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_calendar_outline),
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(BasePadding))
                                Column {
                                    Text(
                                        text = "Data si ora",
                                        style = titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = "Joi, 1 August 14:30",
                                        style = bodyLarge,
                                        color = Color.Gray
                                    )
                                }
                            }

                            Spacer(Modifier.height(SpacingXL))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_shopping_outline),
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(BasePadding))
                                Column {
                                    Text(
                                        text = stringResource(R.string.service),
                                        style = titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = product.name,
                                        style = bodyLarge,
                                        color = Color.Gray
                                    )
                                }
                            }

                            Spacer(Modifier.height(SpacingXL))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_person_outline),
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(BasePadding))
                                Column {
                                    Text(
                                        text = "Specialist",
                                        style = titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = "Raducu Balgiu",
                                        style = bodyLarge,
                                        color = Color.Gray
                                    )
                                }
                            }

                            Spacer(Modifier.height(SpacingXXL))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Pret",
                                        style = titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 18.sp
                                    )
                                    Text(text = "100 RON")
                                }
                                VerticalDivider()
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Pret",
                                        style = titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 18.sp
                                    )
                                    Text(text = "100 RON")
                                }
                            }
                        }
                    }
                }
            }
            Column {
                HorizontalDivider(color = Divider, thickness = 0.55.dp)
                Spacer(Modifier.height(BasePadding))
                MainButton(
                    modifier = Modifier.padding(horizontal = BasePadding),
                    title = stringResource(R.string.book),
                    onClick = onSubmit
                )
            }
        }
    }
}