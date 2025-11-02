package com.example.scrollbooker.entity.booking.employee.data.mappers

import com.example.scrollbooker.entity.booking.employee.data.remote.EmployeeDto
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee

fun EmployeeDto.toDomain(): Employee {
    return Employee(
        id = id,
        fullName = fullName,
        username = username,
        avatar = avatar,
        job = job,
        hireDate = hireDate,
        ratingsAverage = ratingsAverage,
        followersCount = followersCount,
        ratingsCount = ratingsCount,
        productsCount = productsCount
    )
}