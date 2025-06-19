package com.example.scrollbooker.shared.currency.domain.useCase
import com.example.scrollbooker.shared.currency.data.remote.CurrencyDto
import com.example.scrollbooker.shared.currency.domain.repository.CurrencyRepository
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class GetUserCurrenciesUseCase @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(): Result<List<CurrencyDto>> {
        val userId = authDataStore.getUserId().firstOrNull()

        if(userId == null) {
            Timber.tag("User Currencies").e("User Id not found in Data Store")
            throw IllegalStateException("User Id not found")
        }

        return repository.getUserCurrencies(userId)
    }
}