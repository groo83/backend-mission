package com.murple.chat.domain.user.dto

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.murple.chat.common.code.ErrorCode
import com.murple.chat.common.exception.InvalidValueException
import com.murple.chat.domain.user.Phone

data class PhoneDto(
    val number: String,
    val countryCode: String,
    val label: String,
    val isCertified: Boolean
) {
    init {
        val phoneNumberUtil = PhoneNumberUtil.getInstance()

        try {
            val number = phoneNumberUtil.parse(number, "KR")
            phoneNumberUtil.isValidNumber(number)  // 유효한 번호인지 검사
        } catch (e: Exception) {
            InvalidValueException(ErrorCode.INVALID_PARAMETER_PHONE_NUMBER)
        }
    }

    companion object {
        fun from(phone: Phone): PhoneDto {
            return PhoneDto(
                number = phone.number,
                countryCode = phone.countryCode!!,
                label = phone.label.toString(),
                isCertified = phone.isCertified

            )
        }
    }
}