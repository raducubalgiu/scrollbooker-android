package com.example.scrollbooker.shared.currencies.data.repository
import com.example.scrollbooker.shared.currencies.data.remote.CurrenciesApiService
import com.example.scrollbooker.shared.currencies.domain.repository.CurrencyRepository
import com.example.scrollbooker.shared.currencies.data.remote.CurrencyDto
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