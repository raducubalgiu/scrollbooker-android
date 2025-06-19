package com.example.scrollbooker.shared.currency.data.repository
import com.example.scrollbooker.shared.currency.data.remote.CurrenciesApiService
import com.example.scrollbooker.shared.currency.domain.repository.CurrencyRepository
import com.example.scrollbooker.shared.currency.data.remote.CurrencyDto
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val apiService: CurrenciesApiService
): CurrencyRepository {
    override suspend fun getAllCurrencies(): Result<List<CurrencyDto>> = runCatching {
        apiService.getAllCurrencies()
    }

    override suspend fun getUserCurrencies(userId: Int): Result<List<CurrencyDto>> = runCatching {
       apiService.getUserCurrencies(userId)
    }

}