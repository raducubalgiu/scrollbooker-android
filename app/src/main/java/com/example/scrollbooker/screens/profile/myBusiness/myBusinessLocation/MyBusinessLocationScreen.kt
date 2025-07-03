package com.example.scrollbooker.screens.profile.myBusiness.myBusinessLocation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.SearchBar
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.FeatureState

@Composable
fun MyBusinessLocationScreen(
    viewModel: MyBusinessLocationViewModel,
    onBack: () -> Unit,
    onNextOrSave: () -> Unit,
) {
    val searchState by viewModel.searchState.collectAsState()
    var query by remember { mutableStateOf("") }

    FormLayout(
        headLine = stringResource(id = R.string.location),
        subHeadLine = stringResource(id = R.string.addYourBusinessLocation),
        buttonTitle = stringResource(id = R.string.nextStep),
        onBack = onBack,
        onNext = onNextOrSave,
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(
                start = SpacingXL,
                end = SpacingXXL,
                bottom = BasePadding
            )
        ) {
            SearchBar(
                value = query,
                onValueChange = {
                    query = it
                    viewModel.searchAddress(query)
                },
                placeholder = "Cauta"
            )

            Spacer(Modifier.height(BasePadding))

            when(val state = searchState) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Success -> {
                    val results = state.data
                    if (results.isEmpty()) {
                        Text("Nici o adress gasita")
                    } else {
                        LazyColumn {
                            items(results) { address ->
                                Text(
                                    text = address.fullAddress,
                                    modifier = Modifier.fillMaxWidth().padding(12.dp)
                                )
                            }
                        }
                    }
                }
                is FeatureState.Error -> Unit
                null -> Unit
            }

//            Column {
//                Row(modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    OutlinedTextField(
//                        value = "",
//                        onValueChange = { },
//                        label = { Text("Strada") },
//                        enabled = false,
//                        modifier = Modifier.weight(2f),
//                        shape = ShapeDefaults.Small
//                    )
//
//                    OutlinedTextField(
//                        value = "",
//                        onValueChange = { },
//                        label = { Text("Numar") },
//                        enabled = false,
//                        modifier = Modifier.weight(2f),
//                        shape = ShapeDefaults.Small
//                    )
//                }
//
//                Spacer(Modifier.height(8.dp))
//
//                Row(modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    OutlinedTextField(
//                        value = "",
//                        onValueChange = { },
//                        label = { Text("Oras") },
//                        enabled = false,
//                        modifier = Modifier.weight(2f),
//                        shape = ShapeDefaults.Small
//                    )
//
//                    OutlinedTextField(
//                        value = "",
//                        onValueChange = { },
//                        label = { Text("Judet") },
//                        enabled = false,
//                        modifier = Modifier.weight(2f),
//                        shape = ShapeDefaults.Small
//                    )
//                }
//
//                Spacer(Modifier.height(8.dp))
//
//                OutlinedTextField(
//                    value = "",
//                    onValueChange = { },
//                    label = { Text("Judet") },
//                    enabled = false,
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = ShapeDefaults.Small
//                )
//            }
        }
    }
}