package com.example.scrollbooker.ui.myBusiness.mySubscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.LastMinute
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import kotlin.math.absoluteValue

data class SubscriptionPlan(
    val id: Int,
    val name: String,
    val price: String,
    val originalPrice: String? = null,
    val features: List<SubscriptionFeature>,
    val isFreeTrialAvailable: Boolean = false
)

data class SubscriptionFeature(
    val description: String,
    val planIds: List<Int> = emptyList(),
    val displayAllPlans: Boolean = true,
    val isFreeFeature: Boolean,
    val oldValue: String? = null,
    val newValue: String? = null
)

@Composable
fun MySubscriptionScreen(
    viewModel: MySubscriptionViewModel,
    onBack: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 1) { 2 }

    val features = listOf(
        SubscriptionFeature(
            description = "Vizibilitate pe harta",
            planIds = listOf(1, 2),
            isFreeFeature = true
        ),
        SubscriptionFeature(
            description = "Sistem de gestionare rezervari si calendar",
            planIds = listOf(1, 2),
            isFreeFeature = true
        ),
        SubscriptionFeature(
            description = "Suport prin email",
            planIds = listOf(1, 2),
            isFreeFeature = true
        ),
        SubscriptionFeature(
            description = "Statistici de baza",
            planIds = listOf(1, 2),
            isFreeFeature = true
        ),
        SubscriptionFeature(
            description = "comision pentru rezervarile generate de clienti noi",
            planIds = listOf(1),
            displayAllPlans = false,
            isFreeFeature = false,
            oldValue = "10%",
            newValue = "10%"
        ),
        SubscriptionFeature(
            description = "comision pentru rezervarile generate de clienti noi",
            planIds = listOf(2),
            displayAllPlans = false,
            isFreeFeature = false,
            oldValue = "10%",
            newValue = "5%"
        ),
        SubscriptionFeature(
            description = "postare video/saptamana per business/angajat",
            planIds = listOf(1),
            displayAllPlans = false,
            isFreeFeature = false,
            oldValue = "1",
            newValue = "1"
        ),
        SubscriptionFeature(
            description = "postari video/saptamana per business/angajat",
            planIds = listOf(2),
            displayAllPlans = false,
            isFreeFeature = false,
            oldValue = "1",
            newValue = "2",
        ),
        SubscriptionFeature(
            description = "Afisare numar de telefon public",
            planIds = listOf(2),
            isFreeFeature = false
        ),
        SubscriptionFeature(
            description = "Adaugare reduceri pe intervale orare",
            planIds = listOf(2),
            isFreeFeature = false
        ),
    )

    val subscriptions = listOf(
        SubscriptionPlan(
            id = 1,
            name = "Free",
            price = "Gratuit",
            features = features,
        ),
        SubscriptionPlan(
            id = 2,
            name = "Standard",
            price = "0 RON",
            originalPrice = "299 RON/lună",
            features = features,
            isFreeTrialAvailable = true
        ),
    )

    Layout(
        onBack = onBack,
        enablePaddingH = false,
        headerTitle = "Abonamente",
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.fillMaxSize().background(SurfaceBG)) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize(),
                        pageSpacing = 8.dp,
                        contentPadding = PaddingValues(horizontal = 48.dp),
                    ) { page ->
                        // Calculate page offset for animations
                        val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                        // Scale: current page = 1.0, other pages = 0.85
                        val scale = 1f - (pageOffset.absoluteValue * 0.15f).coerceIn(0f, 0.15f)

                        val plan = subscriptions[page]

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.9f)
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                },
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Background),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Column(Modifier.weight(1f)) {
                                    // Header section with colored background
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                LastMinute, RoundedCornerShape(
                                                    topStart = 24.dp, topEnd = 24.dp
                                                ))
                                            .padding(vertical = 24.dp, horizontal = BasePadding)
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = plan.name,
                                                fontSize = 32.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = OnPrimary
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            // Free trial badge for Standard
                                            if(plan.isFreeTrialAvailable) {
                                                Box(
                                                    modifier = Modifier
                                                        .background(OnPrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                                ) {
                                                    Text(
                                                        text = "🎉 Perioada gratuită: 3 luni",
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(12.dp))
                                            }

                                            // Original price (strikethrough)
                                            if(plan.originalPrice != null) {
                                                Text(
                                                    text = plan.originalPrice,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = Color.White.copy(alpha = 0.7f),
                                                    textDecoration = TextDecoration.LineThrough
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                            }

                                            // Current price
                                            Text(
                                                text = plan.price,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }

                                    // Features section
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(BasePadding)
                                    ) {
                                        val basicFeatures = features.filter { it.isFreeFeature == true && plan.id in it.planIds }
                                        val paidFeatures = features.filter { it.isFreeFeature == false && plan.id in it.planIds }

                                        Column {
                                            basicFeatures.forEach { feature ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 8.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Outlined.Check,
                                                        contentDescription = null,
                                                        tint = LastMinute,
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(12.dp))
                                                    Text(
                                                        text = feature.description,
                                                        fontSize = 16.sp,
                                                        color = OnBackground
                                                    )
                                                }
                                            }

                                            HorizontalDivider(
                                                color = Divider,
                                                thickness = 0.6.dp,
                                            )

                                            paidFeatures.forEach { feature ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 8.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Outlined.Check,
                                                        contentDescription = null,
                                                        tint = if(plan.id in feature.planIds) LastMinute else Color.Gray,
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(12.dp))
                                                    Text(
                                                        text = if(feature.newValue != null) "${feature.newValue} ${feature.description}"
                                                        else feature.description,
                                                        fontSize = 16.sp,
                                                        color = OnBackground
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                MainButton(
                                    modifier = Modifier.padding(BasePadding),
                                    title = "Alege ${subscriptions[pagerState.currentPage].name}",
                                    onClick = {}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}