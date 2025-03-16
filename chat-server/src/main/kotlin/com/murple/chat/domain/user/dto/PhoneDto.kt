package com.murple.chat.domain.user.dto

import com.murple.chat.domain.user.Phone

data class PhoneDto(
    val number: String,
    val countryCode: String,
    val label: String,
    val isCertified: Boolean,
    // val id: Long

) {
    companion object {
        fun from(phone: Phone): PhoneDto {
            return PhoneDto(
                number = phone.number,
                countryCode = phone.countryCode!!,
                label = phone.label.toString(),
                isCertified = phone.isCertified,
                //id = phone.id!!

            )
        }
    }
}