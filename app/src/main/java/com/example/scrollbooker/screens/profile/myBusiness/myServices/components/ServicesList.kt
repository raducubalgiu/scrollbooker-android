package com.example.scrollbooker.screens.profile.myBusiness.myServices.components
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.DeletableItem
import com.example.scrollbooker.shared.service.domain.model.Service

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesList(
    modifier: Modifier = Modifier,
    selectedServiceId: String,
    services: List<Service>,
    onSelect: (String) -> Unit,
) {
    var services by remember { mutableStateOf(services) }

    LazyColumn(modifier = modifier) {
        items(services) { service ->
            DeletableItem(
                label = service.name,
                isSelected = selectedServiceId == service.id.toString(),
                onDelete = { onSelect(service.id.toString()) },
            )
        }
    }
}