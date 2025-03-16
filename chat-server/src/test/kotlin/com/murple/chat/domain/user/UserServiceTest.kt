package com.murple.chat.domain.user

import com.murple.chat.common.code.ErrorCode
import com.murple.chat.common.exception.EntityNotFoundException
import com.murple.chat.domain.user.application.UserService
import com.murple.chat.domain.user.dto.UserReqDto
import com.murple.chat.domain.user.enums.Gender
import com.murple.chat.domain.user.infrastructure.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserServiceTest {
    @MockK
    private lateinit var userRepository: UserRepository

    @InjectMockKs
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getUserById throws EntityNotFoundException when user does not exist`() {
        // Given: 특정 ID에 대한 유저가 존재하지 않는 경우
        val userId = 1L

        // Mock 설정: userRepository.findUserById(1L) 호출 시, null 반환하도록 설정
        every { userRepository.findUserById(userId) } returns null

        // When: getUserById 메서드 호출 시 EntityNotFoundException 예외가 발생해야 한다
        val exception = assertThrows<EntityNotFoundException> {
            userService.getUserById(userId)
        }

        // Then: 예외 메시지 및 코드 검증
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.errorCode)
    }

    @Test
    fun `getUserById returns UserResDto when user exists`() {
        // Given: 특정 ID에 대한 유저가 존재한다고 가정
        val userId = 1L
        val user = User(id = userId, name = "John", email = "john@example.com")

        // Mock 설정: userRepository.findUserById(1L) 호출 시, user 객체 반환하도록 설정
        every { userRepository.findUserById(userId) } returns user

        // When: getUserById 메서드 호출
        val result = userService.getUserById(userId)

        // Then: 반환된 UserResDto가 올바른 값인지 검증
        assertNotNull(result)
        assertEquals(userId, result.id)
        assertEquals("John", result.name)

        // Mock을 사용하여 호출된 메서드가 실제로 호출되었는지 확인
        verify { userRepository.findUserById(userId) }
    }

    @Test
    fun `createUser successfully creates a user`() {
        // Given: 새로운 UserReqDto 요청
        val reqDto = UserReqDto(
            name = "John",
            age = 30,
            gender = "MALE",
            email = "john@example.com"
        )

        val user = User(
            id = 1L,
            name = reqDto.name,
            age = reqDto.age,
            gender = Gender.of(reqDto.gender),
            email = reqDto.email
        )

        // Mock 설정: userRepository.save(user) 호출 시, 저장된 user 객체 반환하도록 설정
        every { userRepository.save(any<User>()) } returns user

        // When: createUser 메서드 호출
        val result = userService.createUser(reqDto)

        // Then: 반환된 UserResDto가 올바른 값인지 검증
        assertNotNull(result)
        assertEquals(reqDto.name, result.name)
        assertEquals(reqDto.age, result.age)

        // Mock을 사용하여 호출된 메서드가 실제로 호출되었는지 확인
        verify { userRepository.save(any<User>()) }
    }
}