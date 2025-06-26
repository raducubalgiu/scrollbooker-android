package com.example.scrollbooker.entity.profession.domain.useCase
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.profession.domain.model.Profession
import com.example.scrollbooker.entity.profession.domain.repository.ProfessionRepository
import timber.log.Timber

class GetProfessionsByBusinessTypeUseCase(
    private val repository: ProfessionRepository,
) {
    suspend operator fun invoke(businessTypeId: Int?): FeatureState<List<Profession>> {
        if(businessTypeId == null) {
            Timber.tag("Professions").e("Business Type Id Not Found")
            return FeatureState.Error()
        }

        return try {
            val response = repository.getProfessionsByBusinessTypeId(businessTypeId)
            FeatureState.Success(response)

        } catch (e: Exception) {
            Timber.tag("Professions").e("ERROR: on Fetching Professions: $e")
            FeatureState.Error(e)
        }
    }
}