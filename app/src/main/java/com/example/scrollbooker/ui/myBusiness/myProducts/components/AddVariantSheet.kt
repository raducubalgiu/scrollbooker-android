package com.example.scrollbooker.ui.myBusiness.myProducts.components
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.inputs.Input
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.ui.myBusiness.myProducts.productState.ProductOfferingState
import com.example.scrollbooker.ui.myBusiness.myProducts.productState.ProductVariantState
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVariantSheet(
    sheetState: SheetState,
    employees: FeatureState<List<Employee>>,
    hasEmployees: Boolean?,
    ownerId: Int?,
    defaultName: String,
    onSave: (ProductVariantState) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var variantState by remember {
        mutableStateOf(ProductVariantState(name = defaultName))
    }
    var offeringsInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(hasEmployees, employees) {
        if (offeringsInitialized) return@LaunchedEffect

        val initialOfferings = when (hasEmployees) {
            null -> return@LaunchedEffect

            true -> {
                val employeeList = (employees as? FeatureState.Success)?.data
                    ?: return@LaunchedEffect

                employeeList.map { employee ->
                    ProductOfferingState(
                        userId = employee.id.toString(),
                        price = "",
                        discount = "0",
                        priceWithDiscount = "",
                        isSelected = false
                    )
                }
            }

            false -> {
                val id = ownerId ?: return@LaunchedEffect
                listOf(
                    ProductOfferingState(
                        userId = id.toString(),
                        price = "",
                        discount = "0",
                        priceWithDiscount = "",
                        isSelected = true
                    )
                )
            }
        }

        variantState = variantState.copy(offerings = initialOfferings)
        offeringsInitialized = true
    }

    var showErrors by rememberSaveable { mutableStateOf(false) }
    val validation = remember(variantState) { variantState.validate(context) }

    val offering = variantState.offerings.getOrNull(0)
    val offeringValidation = validation.offeringValidations.getOrNull(0)

    fun updateOffering(index: Int, transform: (ProductOfferingState) -> ProductOfferingState) {
        val updated = variantState.offerings.toMutableList()
        updated.getOrNull(index)?.let { updated[index] = transform(it) }
        variantState = variantState.copy(offerings = updated)
    }

    Sheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onClose = onClose
    ) {
        SheetHeader(
            title = "Adauga varianta",
            onClose = onClose
        )

        Column(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = BasePadding)
            ) {
                Text(
                    text = "Detalii Optiune",
                    style = titleMedium,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(BasePadding))

                Input(
                    label = stringResource(R.string.variantName),
                    value = variantState.name,
                    onValueChange = { variantState = variantState.copy(name = it) },
                    isError = showErrors && validation.nameError != null,
                    errorMessage = validation.nameError.orEmpty(),
                )

                Spacer(Modifier.height(BasePadding))

                Input(
                    label = stringResource(R.string.duration),
                    value = variantState.duration,
                    onValueChange = { variantState = variantState.copy(duration = it) },
                    isError = showErrors && validation.durationError != null,
                    errorMessage = validation.durationError.orEmpty(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    ),
                )

                Spacer(Modifier.height(BasePadding))

                Text(
                    text = "Preturi",
                    style = titleMedium,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(BasePadding))

                if (hasEmployees == true) {
                    BusinessWithEmployeesForm(
                        variantState = variantState,
                        showErrors = showErrors,
                        validation = validation,
                        onToggleSelected = { index -> updateOffering(index) { it.copy(isSelected = !it.isSelected) } },
                        onPriceChange = { index, newPrice ->
                            updateOffering(index) {
                                it.copy(
                                    price = newPrice,
                                    priceWithDiscount = calculatePriceWithDiscount(
                                        newPrice,
                                        it.discount
                                    ).toString()
                                )
                            }
                        },
                        onDiscountChange = { index, newDiscount ->
                            updateOffering(index) {
                                it.copy(
                                    discount = newDiscount,
                                    priceWithDiscount = calculatePriceWithDiscount(
                                        it.price,
                                        newDiscount
                                    ).toString()
                                )
                            }
                        },
                        employeesState = employees
                    )
                } else {
                    BusinessWithoutEmployeesForm(
                        showErrors = showErrors,
                        offering = offering,
                        offeringValidation = offeringValidation,
                        onPriceChange = { newPrice ->
                            updateOffering(0) {
                                it.copy(
                                    price = newPrice,
                                    priceWithDiscount = calculatePriceWithDiscount(
                                        newPrice,
                                        it.discount
                                    ).toString()
                                )
                            }
                        },
                        onDiscountChange = { newDiscount ->
                            updateOffering(0) {
                                it.copy(
                                    discount = newDiscount,
                                    priceWithDiscount = calculatePriceWithDiscount(
                                        it.price,
                                        newDiscount
                                    ).toString()
                                )
                            }
                        }
                    )
                }
            }

            Column {
                HorizontalDivider(color = Divider, thickness = 0.55.dp)
                Spacer(Modifier.height(SpacingM))
                MainButton(
                    modifier = Modifier.padding(horizontal = BasePadding),
                    title = stringResource(R.string.add),
                    enabled = true,
                    onClick = {
                        showErrors = true
                        if (validation.isValid) {
                            onSave(variantState)
                            scope.launch { sheetState.hide() }
                            onClose()
                        }
                    },
                )
            }
        }
    }
}