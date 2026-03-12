package com.example.scrollbooker.ui.shared.booking.steps
import androidx.compose.runtime.Composable
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.ProductSection
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomainWithEmployeeServicesResponse
import com.example.scrollbooker.ui.myBusiness.myProducts.MyProductsContent
import com.example.scrollbooker.ui.myBusiness.myProducts.ServicesTabsState

@Composable
fun BookingProductsStep(
    serviceDomains: FeatureState<ServiceDomainWithEmployeeServicesResponse>,
    products: FeatureState<List<ProductSection>>,
    tabsState: ServicesTabsState,
    onSelectDomain: (Int) -> Unit,
    onSelectService: (domainIndex: Int, serviceIndex: Int) -> Unit,
    onSelectEmployee: (domainIndex: Int, serviceIndex: Int, employeeId: Int) -> Unit
) {
    when(val sDomains = serviceDomains) {
        is FeatureState.Loading -> LoadingScreen()
        is FeatureState.Error -> ErrorScreen()
        is FeatureState.Success -> {
            MyProductsContent(
                serviceDomains = sDomains.data,
                products = products,
                tabsState = tabsState,
                onSelectDomain = onSelectDomain,
                onSelectService = onSelectService,
                onSelectEmployee = onSelectEmployee,
            )
        }
    }
}