package com.example.scrollbooker.ui.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.entity.booking.products.domain.model.ProductOffering
import com.example.scrollbooker.entity.booking.products.domain.model.ProductOfferingUser
import java.math.BigDecimal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.example.scrollbooker.components.core.buttons.MainButton
import kotlinx.coroutines.launch

val dummySelectedItems = listOf(
    SelectedBookingItem(
        productId = 1,
        variantId = 10,
        variantDuration = 30,
        offerings = emptyList(), // Fără reduceri
        productName = "Tuns Clasic + Spălat",
        variantName = "Păr Scurt"
    ),
    SelectedBookingItem(
        productId = 2,
        variantId = 11,
        variantDuration = 15,
        offerings = listOf(
            ProductOffering(
                id = 1,
                user = ProductOfferingUser(
                    id = 13,
                    username = "radu_ion",
                    fullname = "Radu Ion",
                    profession = "Frizer",
                    avatar = null
                ),
                price = BigDecimal(45),
                discount = BigDecimal(0),
                priceWithDiscount = BigDecimal(45)
            )
        ),
        productName = "Aranjat Barbă Ritual",
        variantName = "Prosop cald inclus"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingConfirmationScreen(
    viewModel: BookingViewModel,
    onBack: () -> Unit,
    onConfirmBooking: () -> Unit = {}
) {
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Confirmare Programare",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Înapoi"
                        )
                    }
                }
            )
        },
        bottomBar = {
            MainButton(
                title = "Confirma programarea",
                isLoading = isSaving,
                enabled = !isSaving,
                onClick = {
                    scope.launch {
                        viewModel.createAppointment()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF7F9FA))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 1. SECȚIUNE DATA & ORA (Cel mai important element vizual pentru utilizator)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Data",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Column {
                        // Afișăm variabila din modelul tău 'start_date_locale' sau formatat frumos
                        Text(
                            text = "Luni, 22 Iunie 2026",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "14:30 - 15:15 (45 min)",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )

                        // Badge pentru Last Minute (bazat pe is_last_minute)
                        Box(
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .background(Color(0xFFE8F5E9), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "⚡ Reducere Last Minute aplicată (-15%)",
                                color = Color(0xFF2E7D32),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // 2. SECȚIUNE SPECIALIST (BusinessBookingOwner + BusinessBookingSummary)
            Text(
                "Detalii Specialist & Locație",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Specialist info
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Avatar dummy (Dacă avatar e null, punem un cerc cu inițiale)
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color(0xFFE0E0E0), CircleShape)
                                .border(1.dp, Color(0xFFCCCCCC), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("AM", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Andrei Marinescu", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Senior Barber & Stylist", fontSize = 13.sp, color = Color.Gray)
                        }

                        // Recenzii (ratings_average + ratings_count)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(18.dp)
                            )
                            Text("4.9", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("(124)", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    Divider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Color(0xFFEEEEEE)
                    )

                    // Locație (formatted_address)
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Locație",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                "The Barber Shop SRL",
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                            Text(
                                "Strada Lipscani nr. 24, Etaj 1, Ap. 3, București",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Text(
                text = "Servicii selectate",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    dummySelectedItems.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            // Detalii text serviciu (Nume + Variantă + Durată)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.productName,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Variantă: ${item.variantName} (${item.variantDuration} min)",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }

                            // Preț dinamic (Dummy logic bazat pe prezența elementelor în offerings)
                            Column(horizontalAlignment = Alignment.End) {
                                if (item.offerings.isNotEmpty()) {
                                    // Afișare preț redus
                                    Text(
                                        text = "45.00 RON",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    // Preț vechi tăiat
                                    Text(
                                        text = "55.00 RON",
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                        )
                                    )
                                } else {
                                    // Preț normal standard
                                    Text(
                                        text = "85.00 RON",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }

                        if (index < dummySelectedItems.lastIndex) {
                            Divider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
            }
        }
    }
}