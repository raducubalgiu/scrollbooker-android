package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBusinessClientSheet(
    sheetState: SheetState,
    isSaving: Boolean,
    onSave: (fullname: String, phone: String?) -> Unit,
    onDismiss: () -> Unit,
) {
    var fullname by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    val isFullnameValid = fullname.trim().length >= 3
    var showErrors by rememberSaveable { mutableStateOf(false) }

    ModalBottomSheet(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        containerColor = Background,
        dragHandle = {}
    ) {
        Scaffold(
            containerColor = Background,
            topBar = {
                SheetHeader(
                    title = stringResource(R.string.addNewClient),
                    onClose = onDismiss
                )
            },
            bottomBar = {
                Column(Modifier.imePadding().navigationBarsPadding()) {
                    HorizontalDivider(color = Divider, thickness = 0.55.dp)

                    MainButton(
                        modifier = Modifier.padding(BasePadding),
                        title = stringResource(R.string.save),
                        enabled = !isSaving,
                        isLoading = isSaving,
                        onClick = {
                            showErrors = true
                            if (!isFullnameValid) return@MainButton

                            onSave(fullname.trim(), phone.trim().ifBlank { null })
                        }
                    )
                }
            }
        ) { innerPadding ->
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(horizontal = SpacingXL)
            ) {
                Spacer(Modifier.height(BasePadding))

                Input(
                    label = "${stringResource(R.string.clientName)}*",
                    value = fullname,
                    onValueChange = { fullname = it },
                    isError = showErrors && !isFullnameValid,
                    maxLength = 50,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(Modifier.height(BasePadding))

                Input(
                    label = stringResource(R.string.phone),
                    value = phone,
                    onValueChange = { phone = it },
                    maxLength = 30,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Phone
                    )
                )
            }
        }
    }
}
