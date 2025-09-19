package com.example.scrollbooker.ui.settings.reportProblem.domain.repository

interface ReportProblemRepository  {
    suspend fun sendReportProblem(text: String, userId: Int)
}