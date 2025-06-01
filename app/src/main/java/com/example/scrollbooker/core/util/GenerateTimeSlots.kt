package com.example.scrollbooker.core.util

import com.example.scrollbooker.components.inputs.Option
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

fun generateTimeSlots(stepMinutes: Long = 30): List<Option> {
    val slots = mutableListOf<Option>()
    var currentTime = LocalTime.of(0 , 0)
    val endTime = LocalTime.of(23, 59)

    val formatterValue = DateTimeFormatter.ofPattern("HH:mm:ss")
    val formatterName = DateTimeFormatter.ofPattern("HH:mm")

    while (currentTime <= endTime.minusMinutes(stepMinutes)) {
        val value = currentTime.format(formatterValue)
        val name = currentTime.format(formatterName)

        slots.add(Option(value, name))
        currentTime = currentTime.plusMinutes(stepMinutes)
    }

    return slots
}