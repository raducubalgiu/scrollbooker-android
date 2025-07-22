package com.example.scrollbooker.ui.profile.myProfile.settings.reportProblem.domain.repository

interface ReportProblemRepository  {
    suspend fun sendReportProblem(text: String, userId: Int)
}