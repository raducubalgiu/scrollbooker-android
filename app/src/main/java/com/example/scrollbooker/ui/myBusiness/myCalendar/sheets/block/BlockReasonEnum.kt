package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block

import androidx.annotation.StringRes
import com.example.scrollbooker.R

enum class BlockReasonEnum {
    VACATION,
    MEDICAL_LEAVE,
    DOCTOR_APPOINTMENT,
    LEGAL_DAY_OFF,
    OTHER
}

@StringRes
fun BlockReasonEnum.toLabel(): Int = when(this) {
    BlockReasonEnum.VACATION -> R.string.reason_vacation
    BlockReasonEnum.MEDICAL_LEAVE -> R.string.reason_medical_leave
    BlockReasonEnum.DOCTOR_APPOINTMENT -> R.string.reason_doctor_appointment
    BlockReasonEnum.LEGAL_DAY_OFF -> R.string.reason_legal_day_off
    BlockReasonEnum.OTHER -> R.string.otherReason
}