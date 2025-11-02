package com.example.scrollbooker.entity.nomenclature.problem.domain.repository

interface ReportProblemRepository  {
    suspend fun sendReportProblem(text: String, userId: Int)
}