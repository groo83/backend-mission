package com.murple.chat.domain.user

import com.murple.chat.domain.user.dto.AddressDto
import com.murple.chat.domain.user.dto.PhoneDto
import com.murple.chat.domain.user.dto.UserReqDto
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserReqDtoTest {
    private lateinit var validator: Validator

    @BeforeEach
    fun setUp() {
        val factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    @Test
    fun `UserReqDto should pass validation for valid input`() {
        val validUserReqDto = UserReqDto(
            name = "John Doe",
            age = 25,
            gender = "MALE",
            email = "john.doe@example.com",
            phones = listOf(
                PhoneDto(
                    number = "+821012345678",
                    countryCode = "+82",
                    label = "HOME",
                    isCertified = true
                )
            ),
            addresses = listOf(
                AddressDto(
                    address = "123 Main Street, City",
                    label = "HOME"
                )
            )
        )

        val violations = validator.validate(validUserReqDto)
        assertEquals(0, violations.size)
    }

    @Test
    fun `UserReqDto should fail validation for invalid name`() {
        val invalidUserReqDto = UserReqDto(
            name = "Jo",
            age = 25,
            gender = "MALE",
            email = "john.doe@example.com"
        )

        val violations = validator.validate(invalidUserReqDto)
        assertEquals(1, violations.size)
        assertEquals("이름은 최소 3글자에서 1024글자 사이여야 합니다.", violations.first().message)
    }

}