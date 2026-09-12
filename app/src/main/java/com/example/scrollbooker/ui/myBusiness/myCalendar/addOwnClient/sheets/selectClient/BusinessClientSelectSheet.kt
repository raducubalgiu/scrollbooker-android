package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.selectClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.components.core.inputs.SearchBar
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.businessClient.domain.model.BusinessClient
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider

// Bare content (no ModalBottomSheet of its own) - hosted inside the single shared
// ModalBottomSheet in AddOwnClientScreen, alongside the other addOwnClient sheets.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessClientSelectSheet(
    query: String,
    searchState: FeatureState<List<BusinessClient>>?,
    selectedClient: BusinessClient?,
    onQueryChange: (String) -> Unit,
    onConfirm: (BusinessClient) -> Unit,
    onDismiss: () -> Unit,
) {
    var pendingClient by remember { mutableStateOf(selectedClient) }

    Scaffold(
        containerColor = Background,
        topBar = {
            SheetHeader(
                title = stringResource(R.string.selectClient),
                onClose = onDismiss
            )
        },
        bottomBar = {
            Column(Modifier.imePadding().navigationBarsPadding()) {
                HorizontalDivider(color = Divider, thickness = 0.55.dp)

                MainButton(
                    modifier = Modifier.padding(BasePadding),
                    title = stringResource(R.string.add),
                    enabled = pendingClient != null,
                    onClick = { pendingClient?.let(onConfirm) }
                )
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(Modifier.height(BasePadding))

            SearchBar(
                modifier = Modifier.padding(horizontal = BasePadding),
                value = query,
                onValueChange = onQueryChange,
                placeholder = stringResource(R.string.searchClientByNameOrPhone),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
            )

            Column(
                Modifier
                    .weight(1f)
                    .padding(vertical = BasePadding)
            ) {
                when {
                    query.trim().length < 2 -> Box(
                        modifier = Modifier.fillMaxWidth().padding(BasePadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(R.string.youDidntSearchedInListYet))
                    }

                    searchState is FeatureState.Loading -> LoadingScreen()
                    searchState is FeatureState.Error -> ErrorScreen()
                    searchState is FeatureState.Success -> {
                        val clients = searchState.data

                        if (clients.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(BasePadding),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = stringResource(R.string.notFoundAnyResult))
                            }
                        } else {
                            LazyColumn(Modifier.fillMaxSize()) {
                                items(clients, key = { it.id }) { client ->
                                    InputRadio(
                                        selected = client.id == pendingClient?.id,
                                        onSelect = { pendingClient = client },
                                        headLine = client.fullname,
                                        subHeadline = client.phone
                                    )
                                }
                            }
                        }
                    }

                    else -> Unit
                }
            }
        }
    }
}
