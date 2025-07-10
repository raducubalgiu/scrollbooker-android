package com.example.scrollbooker.entity.currency.data.repository
import com.example.scrollbooker.entity.auth.data.mappers.toDomain
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.currency.data.mapper.toDomain
import com.example.scrollbooker.entity.currency.data.remote.CurrenciesApiService
import com.example.scrollbooker.entity.currency.domain.repository.CurrencyRepository
import com.example.scrollbooker.entity.currency.data.remote.CurrencyDto
import com.example.scrollbooker.entity.currency.data.remote.UserCurrencyUpdateRequest
import com.example.scrollbooker.entity.currency.domain.model.Currency
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

    override suspend fun updateUserCurrencies(currencyIds: List<Int>): AuthState {
        val request = UserCurrencyUpdateRequest(currencyIds)

        return apiService.updateUserCurrencies(request).toDomain()
    }
}