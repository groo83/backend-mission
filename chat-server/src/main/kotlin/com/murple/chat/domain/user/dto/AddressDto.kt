package com.murple.chat.domain.user.dto

import com.murple.chat.common.annoation.ValidEnum
import com.murple.chat.domain.user.Address
import com.murple.chat.domain.user.enums.Label

data class AddressDto(
    val address: String,
    val label: String
) {
    companion object {
        fun from(address: Address): AddressDto {
            return AddressDto(
                label = address.label.toString(),
                address = address.address
            )
        }
    }
}