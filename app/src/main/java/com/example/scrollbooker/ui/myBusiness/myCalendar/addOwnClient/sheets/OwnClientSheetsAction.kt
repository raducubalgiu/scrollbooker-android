package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets
import com.example.scrollbooker.entity.booking.availability.domain.model.Slot
import com.example.scrollbooker.entity.booking.businessClient.domain.model.BusinessClient
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import org.threeten.bp.LocalDate

sealed interface OwnClientSheetsAction {
    data object Dismiss: OwnClientSheetsAction
    data class ClientQueryChange(val query: String): OwnClientSheetsAction
    data class ConfirmClient(val client: BusinessClient): OwnClientSheetsAction
    data class SaveNewClient(val fullname: String, val phone: String?): OwnClientSheetsAction
    data class ConfirmServices(val products: Set<Product>): OwnClientSheetsAction
    data class DayClick(val date: LocalDate): OwnClientSheetsAction
    data class ConfirmSlot(val slot: Slot): OwnClientSheetsAction
}
