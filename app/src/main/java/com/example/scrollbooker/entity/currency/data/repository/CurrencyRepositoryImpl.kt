package com.example.scrollbooker.entity.currency.data.repository
import com.example.scrollbooker.entity.currency.data.remote.CurrenciesApiService
import com.example.scrollbooker.entity.currency.domain.repository.CurrencyRepository
import com.example.scrollbooker.entity.currency.data.remote.CurrencyDto
import kotlinx.coroutines.delay
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val apiService: CurrenciesApiService
): CurrencyRepository {
    override suspend fun getAllCurrencies(): Result<List<CurrencyDto>> = runCatching {
        delay(300)
        apiService.getAllCurrencies()
    }

    override suspend fun getUserCurrencies(userId: Int): Result<List<CurrencyDto>> = runCatching {
       apiService.getUserCurrencies(userId)
    }

}