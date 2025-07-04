package com.example.scrollbooker.entity.currency.data.repository
import com.example.scrollbooker.entity.auth.data.mappers.toDomain
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.currency.data.remote.CurrenciesApiService
import com.example.scrollbooker.entity.currency.domain.repository.CurrencyRepository
import com.example.scrollbooker.entity.currency.data.remote.CurrencyDto
import com.example.scrollbooker.entity.currency.data.remote.UserCurrencyUpdateRequest
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

    override suspend fun updateUserCurrencies(currencyIds: List<Int>): AuthState {
        val request = UserCurrencyUpdateRequest(currencyIds)

        return apiService.updateUserCurrencies(request).toDomain()
    }
}