package com.example.scrollbooker.entity.booking.employmentRequest.domain.model
import com.example.scrollbooker.entity.nomenclature.profession.domain.model.Profession
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import org.threeten.bp.ZonedDateTime

data class EmploymentRequest(
    val id: Int,
    val createdAt: ZonedDateTime,
    val status: String,
    val employee: UserSocial,
    val employer: UserSocial,
    val profession: Profession
)