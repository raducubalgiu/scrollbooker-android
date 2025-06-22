package com.example.scrollbooker.screens.profile.myBusiness.myBusinessLocation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.SearchBar
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL

@Composable
fun MyBusinessLocationScreen(
    viewModel: MyBusinessLocationViewModel,
    onBack: () -> Unit,
    onNextOrSave: () -> Unit,
) {
    var query by remember { mutableStateOf("") }
//    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
//
//    val debounceScope = rememberCoroutineScope()
//    var debounceJob by remember { mutableStateOf<Job?>(null) }

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

//                    debounceJob?.cancel()
//
//                    debounceJob = debounceScope.launch {
//                        delay(400)
//                        if(query.length > 2) {
//                                val token = AutocompleteSessionToken.newInstance()
//                                val request = FindAutocompletePredictionsRequest.builder()
//                                    .setSessionToken(token)
//                                    .setQuery(query)
//                                    .build()
//
//                        } else {
//                            predictions = emptyList()
//                        }
//                    }
                },
                placeholder = "Cauta"
            )

            Spacer(Modifier.height(BasePadding))

            Column {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        label = { Text("Strada") },
                        enabled = false,
                        modifier = Modifier.weight(2f),
                        shape = ShapeDefaults.Small
                    )

                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        label = { Text("Numar") },
                        enabled = false,
                        modifier = Modifier.weight(2f),
                        shape = ShapeDefaults.Small
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        label = { Text("Oras") },
                        enabled = false,
                        modifier = Modifier.weight(2f),
                        shape = ShapeDefaults.Small
                    )

                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        label = { Text("Judet") },
                        enabled = false,
                        modifier = Modifier.weight(2f),
                        shape = ShapeDefaults.Small
                    )
                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    label = { Text("Judet") },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    shape = ShapeDefaults.Small
                )
            }
        }
    }
}