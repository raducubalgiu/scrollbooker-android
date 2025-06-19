package com.example.scrollbooker.screens.profile.settings.reportProblem.domain.repository

interface ReportProblemRepository  {
    suspend fun sendReportProblem(text: String, userId: Int)
}