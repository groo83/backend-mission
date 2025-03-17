package com.murple.chat.domain.user.dto

import com.murple.chat.domain.user.User
import java.time.LocalDateTime

data class UserResDto(
    val id: Long,
    val name: String,
    val age: Int? = null,
    val gender: String? = null,
    val email: String? = null,
    val phones: List<PhoneDto> = emptyList(),
    val addresses: List<AddressDto> = emptyList(),
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
    companion object {
        fun from(user: User): UserResDto {
            return UserResDto(
                id = user.id!!,
                name = user.name,
                age = user.age,
                gender = user.gender?.toString(),
                email = user.email,
                phones = user.phones.map { x -> PhoneDto.from(x) },
                addresses = user.addresses.map { x -> AddressDto.from(x) },
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        }
    }
}