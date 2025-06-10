package com.example.scrollbooker.feature.myBusiness.employeeDismissal.data.mappers

import com.example.scrollbooker.feature.myBusiness.employeeDismissal.data.remote.EmployeeDismissalDto
import com.example.scrollbooker.feature.myBusiness.employeeDismissal.domain.model.EmployeeDismissal

fun EmployeeDismissalDto.toDomain(): EmployeeDismissal {
    return EmployeeDismissal(
        id = id,
        title = title,
        text = text,
        version = version,
        createdAt = createdAt
    )
}