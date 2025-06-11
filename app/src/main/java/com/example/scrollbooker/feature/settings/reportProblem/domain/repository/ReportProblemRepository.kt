package com.example.scrollbooker.feature.settings.reportProblem.domain.repository

interface ReportProblemRepository  {
    suspend fun sendReportProblem(text: String, userId: Int)
}