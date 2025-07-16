package com.example.scrollbooker.screens.profile.myBusiness.myEmploymentRequests
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.components.core.inputs.SearchBar
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.FeatureState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.scrollbooker.core.util.Dimens.BasePadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EmploymentSelectEmployeeScreen(
    viewModel: EmploymentRequestsViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    val usersState by viewModel.searchUsersClientsState.collectAsState()
    val currentQuery by viewModel.currentQuery.collectAsState()
    val selectedEmployee by viewModel.selectedEmployee.collectAsState()

    LaunchedEffect(Unit) {
        delay(200)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    DisposableEffect(Unit) {
        onDispose {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }

    FormLayout(
        modifier = Modifier.safeDrawingPadding(),
        headLine = "Selecteaza angajat",
        subHeadLine = "Cauta angajatul in lista de mai jos",
        onBack = {
            coroutineScope.launch {
                focusManager.clearFocus()
                keyboardController?.hide()
                delay(150)
                onBack()
            }
        },
        buttonTitle = stringResource(R.string.nextStep),
        onNext = {
            coroutineScope.launch {
                focusManager.clearFocus()
                keyboardController?.hide()
                delay(150)
                onNext()
            }
        },
        enableBottomAction = selectedEmployee != null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = {
                        coroutineScope.launch {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            delay(150)
                        }
                    },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        start = SpacingXL,
                        end = SpacingXXL,
                        bottom = SpacingS
                    )
            ) {
                SearchBar(
                    modifier = Modifier
                        .focusRequester(focusRequester),
                    value = currentQuery,
                    onValueChange = {
                        viewModel.searchEmployees(it)
                    },
                    placeholder = stringResource(R.string.search)
                )
            }

            if(currentQuery.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(BasePadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Inca nu ai cautat angajatul"
                    )
                }
            }

            when(usersState) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Success<*> -> {
                    val users = (usersState as FeatureState.Success).data

                    LazyColumn(
                        Modifier.weight(1f)
                    ) {
                        items(users) { user ->
                            InputRadio(
                                selected = user.id == selectedEmployee?.id,
                                onSelect = {
                                    viewModel.setSelectedEmployee(user)
                                },
                                headLine = user.fullName,
                            )
                        }

                        item {
                            if(users.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(BasePadding),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Nu a fost gasit nici un rezultat"
                                    )
                                }
                            }
                        }
                    }
                }
                else -> Unit
            }
        }
    }
}