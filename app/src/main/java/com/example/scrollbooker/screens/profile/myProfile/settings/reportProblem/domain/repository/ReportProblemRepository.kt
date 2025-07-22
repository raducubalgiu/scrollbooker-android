package com.example.scrollbooker.screens.profile.myProfile.settings.reportProblem.domain.repository

interface ReportProblemRepository  {
    suspend fun sendReportProblem(text: String, userId: Int)
}