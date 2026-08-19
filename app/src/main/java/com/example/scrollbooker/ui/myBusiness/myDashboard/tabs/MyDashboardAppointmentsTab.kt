package com.example.scrollbooker.ui.myBusiness.myDashboard.tabs
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.components.customized.stats.DonutChart
import com.example.scrollbooker.components.customized.stats.DonutChartEntry
import com.example.scrollbooker.components.customized.stats.StatBarRow
import com.example.scrollbooker.components.customized.stats.StatCard
import com.example.scrollbooker.components.customized.stats.StatTitle
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.myBusiness.myDashboard.DashboardPeriod
import com.example.scrollbooker.ui.myBusiness.myDashboard.components.PeriodSelector
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG

data class AppointmentSource(
    val source: BookingSourceEnum,
    val value: String,
    val progress: Float
)

@Composable
fun MyDashboardAppointmentsTab() {
    var selectedPeriod by remember { mutableStateOf(DashboardPeriod.SEVEN_DAYS) }

    val dummyAppointmentSources = listOf(
        AppointmentSource(source = BookingSourceEnum.PROFILE, value = "150", progress = 1.0f),
        AppointmentSource(source = BookingSourceEnum.BOOK_AGAIN, value = "98", progress = 0.65f),
        AppointmentSource(source = BookingSourceEnum.SEARCH, value = "74", progress = 0.49f),
        AppointmentSource(source = BookingSourceEnum.EXPLORE_FEED, value = "45", progress = 0.30f),
        AppointmentSource(source = BookingSourceEnum.PROFILE_GRID_POST_DETAIL, value = "30", progress = 0.20f),
        AppointmentSource(source = BookingSourceEnum.FOLLOWING_FEED, value = "22", progress = 0.14f),
        AppointmentSource(source = BookingSourceEnum.SEARCH_BUSINESS_PROFILE, value = "12", progress = 0.08f),
        AppointmentSource(source = BookingSourceEnum.PROFILE_BOOKMARKS_POST_DETAIL, value = "5", progress = 0.03f)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        PeriodSelector(
            selectedPeriod = selectedPeriod,
            onPeriodSelected = { selectedPeriod = it }
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(SpacingS),
            contentPadding = PaddingValues(horizontal = SpacingS),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(ShapeDefaults.Medium)
                        .background(Background)
                        .padding(BasePadding)
                ) {
                    StatTitle(
                        modifier = Modifier.padding(bottom = SpacingM),
                        title = "Key metrics",
                        onClick = {}
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(SpacingM)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            label = "Rezervari",
                            value = "100",
                            containerColor = Color.Transparent,
                            contentColor = OnBackground,
                            borderColor = Divider
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            label = "Incasari",
                            value = "1,200 RON",
                            containerColor = Color.Transparent,
                            contentColor = OnBackground,
                            borderColor = Divider
                        )
                    }

                    Spacer(Modifier.height(SpacingM))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(SpacingM)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            label = "Finalizate",
                            value = "90",
                            containerColor = Color.Transparent,
                            contentColor = OnBackground,
                            borderColor = Divider
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            label = "Anulate",
                            value = "10",
                            containerColor = Color.Transparent,
                            contentColor = OnBackground,
                            borderColor = Divider
                        )
                    }

                    Spacer(Modifier.height(SpacingM))

                    DonutChart(
                        title = "Canal",
                        entries = listOf(
                            DonutChartEntry(label = "Scrollbooker", value = 30, color = Primary),
                            DonutChartEntry(label = "Client Propriu", value = 200, color = SurfaceBG)
                        )
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(ShapeDefaults.Medium)
                        .background(Background)
                        .padding(BasePadding)
                ) {
                    StatTitle(
                        modifier = Modifier.padding(bottom = SpacingM),
                        title = "Surse rezervari",
                        onClick = {}
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(SpacingM),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        dummyAppointmentSources.forEach { appointmentSource ->
                            StatBarRow(
                                label = appointmentSource.source.key,
                                valueString = appointmentSource.value,
                                progressPercentage = appointmentSource.progress
                            )
                        }
                    }
                }
            }
        }
    }
}
