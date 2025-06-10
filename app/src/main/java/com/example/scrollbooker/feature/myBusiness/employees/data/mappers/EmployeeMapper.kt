package com.example.scrollbooker.feature.myBusiness.employees.data.mappers

import com.example.scrollbooker.feature.myBusiness.employees.data.remote.EmployeeDto
import com.example.scrollbooker.feature.myBusiness.employees.domain.model.Employee

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