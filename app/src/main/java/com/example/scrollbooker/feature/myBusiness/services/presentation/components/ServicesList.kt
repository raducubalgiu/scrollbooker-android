package com.example.scrollbooker.feature.myBusiness.services.presentation.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.DeletableItem
import com.example.scrollbooker.components.DialogConfirm
import com.example.scrollbooker.feature.myBusiness.services.domain.model.Service

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesList(
    services: List<Service>
) {
    var services by remember { mutableStateOf(services) }
    var selectedService by remember { mutableStateOf("") }
    var isOpen by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if(isOpen) {
            DialogConfirm(
                title = selectedService,
                text = "Esti sigur ca vrei sa stergi acest serviciu",
                onDismissRequest = {
                    selectedService = ""
                    isOpen = false
                },
                onConfirmation = {
                    if(selectedService.isNotEmpty()) {
                        services = services.filterNot { it.name == selectedService }
                    }
                    isOpen = false
                }
            )
        }

        LazyColumn(Modifier.weight(1f)) {
            items(services) { service ->
                DeletableItem(
                    label = service.name,
                    isSelected = selectedService == service.name,
                    onDelete = {
                        selectedService = it
                        isOpen = true
                    },
                )
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            enabled = true
        ) {
            Text(text = stringResource(R.string.add))
        }
    }
}