package com.example.scrollbooker.ui.profile.myProfile.settings.reportProblem.data.repository
import com.example.scrollbooker.ui.profile.myProfile.settings.reportProblem.data.remote.ReportProblemApiService
import com.example.scrollbooker.ui.profile.myProfile.settings.reportProblem.data.remote.ReportProblemRequest
import com.example.scrollbooker.ui.profile.myProfile.settings.reportProblem.domain.repository.ReportProblemRepository
import javax.inject.Inject

class ReportProblemRepositoryImpl @Inject constructor(
    private val apiService: ReportProblemApiService
): ReportProblemRepository {
    override suspend fun sendReportProblem(text: String, userId: Int) {
        val request = ReportProblemRequest(text, userId)

        return apiService.sendProblem(request)
    }

}