package com.example.scrollbooker.shared.employee.data.mappers

import com.example.scrollbooker.shared.employee.data.remote.EmployeeDto
import com.example.scrollbooker.shared.employee.domain.model.Employee

fun EmployeeDto.toDomain(): Employee {
    return Employee(
        username = username,
        job = job,
        hireDate = hireDate,
        followersCount = followersCount,
        ratingsCount = ratingsCount,
        ratingsAverage = ratingsAverage
    )
}