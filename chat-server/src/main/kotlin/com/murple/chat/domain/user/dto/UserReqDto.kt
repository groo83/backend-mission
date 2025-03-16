package com.murple.chat.domain.user.dto

data class UserReqDto(
    val name: String,
    val age: Int? = null,
    val gender: String? = null,
    val email: String? = null,
    val phones: List<PhoneDto>? = null,
    val addresses: List<AddressDto>? = null
) {
}