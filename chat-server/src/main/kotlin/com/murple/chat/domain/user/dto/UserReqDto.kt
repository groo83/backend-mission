package com.murple.chat.domain.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

data class UserReqDto(
    @field:Size(min = 3, max = 1024, message = "이름은 최소 3글자에서 1024글자 사이여야 합니다.")
    val name: String,
    @field:Min(0, message = "나이는 0 이상이어야 합니다.")
    val age: Int? = null,
    val gender: String? = null,
    @field:Email(message = "올바르지 않은 이메일 형식입니다.")
    val email: String? = null,
    @field:Size(max = 8, message = "전화번호는 최대 8개까지 입력 가능합니다.")
    val phones: List<PhoneDto>? = null,
    @field:Size(max = 8, message = "주소는 최대 8개까지 입력 가능합니다.")
    val addresses: List<AddressDto>? = null
)