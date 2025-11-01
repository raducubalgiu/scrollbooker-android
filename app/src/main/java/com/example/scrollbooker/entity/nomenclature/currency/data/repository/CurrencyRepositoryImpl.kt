package com.example.scrollbooker.entity.nomenclature.currency.data.repository
import com.example.scrollbooker.entity.nomenclature.currency.data.mapper.toDomain
import com.example.scrollbooker.entity.nomenclature.currency.data.remote.CurrenciesApiService
import com.example.scrollbooker.entity.nomenclature.currency.data.remote.UserCurrencyUpdateRequest
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency
import com.example.scrollbooker.entity.nomenclature.currency.domain.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val apiService: CurrenciesApiService
): CurrencyRepository {
    override suspend fun getAllCurrencies(): Result<List<Currency>> = runCatching {
        apiService.getAllCurrencies().map { it.toDomain() }
    }

    override suspend fun getUserCurrencies(userId: Int): Result<List<Currency>> = runCatching {
       apiService.getUserCurrencies(userId).map { it.toDomain() }
    }

    override suspend fun updateUserCurrencies(currencyIds: List<Int>) {
        val request = UserCurrencyUpdateRequest(currencyIds)

        return apiService.updateUserCurrencies(request)
    }
}