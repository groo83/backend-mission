package com.murple.chat.domain.user

import com.murple.chat.common.code.ErrorCode
import com.murple.chat.common.exception.EntityNotFoundException
import com.murple.chat.domain.user.application.UserService
import com.murple.chat.domain.user.dto.AddressDto
import com.murple.chat.domain.user.dto.PhoneDto
import com.murple.chat.domain.user.dto.UserReqDto
import com.murple.chat.domain.user.dto.UserResDto
import com.murple.chat.domain.user.enums.Gender
import com.murple.chat.domain.user.enums.Label
import com.murple.chat.domain.user.infrastructure.UserRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
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

    @Nested
    inner class GetUserTests {
        @Test
        fun `getUserById - successfully return a user`() {
            // given
            val userId = 1L
            val existingUser = User(
                id = userId,
                name = "John Doe",
                age = 28,
                gender = Gender.MALE,
                email = "john.doe@example.com",
                phones = mutableListOf(Phone(number =  "+8201011112222", countryCode = "KR", label = Label.WORK, isCertified = true)),
                addresses = mutableListOf(Address(label = Label.HOME, address = "123 Main St"))
            )

            // UserResDto.from()가 예상대로 동작하는지 확인하기 위해 mockk 사용
            val expectedUserResDto = UserResDto(
                id = userId,
                name = "John Doe",
                age = 28,
                gender = Gender.MALE.code,
                email = "john.doe@example.com",
                phones = listOf(PhoneDto(number =  "+8201011112222", countryCode = "KR", label = Label.WORK.code, isCertified = true)),
                addresses = listOf(AddressDto(label = Label.HOME.code, address = "123 Main St"))
            )

            every { userRepository.findUserById(userId) } returns existingUser

            // when
            val result = userService.getUserById(userId)

            // then
            assertEquals(expectedUserResDto.id, result.id)
            assertEquals(expectedUserResDto.name, result.name)
            assertEquals(expectedUserResDto.gender, result.gender)
            assertEquals(expectedUserResDto.email, result.email)
            assertEquals(expectedUserResDto.phones, result.phones)
            assertEquals(expectedUserResDto.addresses, result.addresses)
        }

        @Test
        fun `getUserById - throws EntityNotFoundException when user does not exist`() {
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
        fun `getUserById - returns UserResDto when user exists`() {
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
    }

    @Nested
    inner class CreateUserTests {
        @Test
        fun `createUser - successfully creates a user`() {
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

    @Test
    fun `findUsersByNamesOrderByCreatedAtAsc - successfully find createdAtAsc top5`() {
        // Given
        val names = listOf("홍길동", "김영희")
        val mockUsers = listOf(
            User(
                id = 1L,
                name = "홍길동",
                age = 40,
                gender = Gender.MALE,
                email = "gildong1@example.com",
            ),
            User(
                id = 2L,
                name = "김영희",
                age = 40,
                gender = Gender.FEMALE,
                email = "yhee@example.com"
            ),
            User(
                id = 3L,
                name = "홍길동",
                age = 40,
                gender = Gender.MALE,
                email = "gildong3@example.com"
            )
        )
        every { userRepository.findUsersByNamesOrderByCreatedAtAsc(names) } returns mockUsers

        // When
        val result = userService.findUsersByNamesOrderByCreatedAtAsc(names)

        // Then
        assertEquals(3, result.size)
        assertEquals("홍길동", result[0].name)
        assertEquals("김영희", result[1].name)
        assertEquals("홍길동", result[2].name)
    }

    @Nested
    inner class UpdateUserTests {
        @Test
        fun `updateUser - successfully update a user`() {
            // Given
            val userId = 1L
            val existingUser = User(
                id = userId,
                name = "Old Name",
                age = 30,
                gender = Gender.MALE,
                email = "old@example.com",
                phones = mutableListOf(
                    Phone(
                        label = Label.WORK,
                        number = "+821012345678",
                        countryCode = "KR",
                        isCertified = true
                    )
                ),
                addresses = mutableListOf(Address(label = Label.HOME, address = "Old Address"))
            )

            val updateDto = UserReqDto(
                name = "New Name",
                age = 35,
                gender = "FEMALE",
                email = "new@example.com",
                phones = listOf(
                    PhoneDto(
                        label = "HOME",
                        number = "+87654321",
                        countryCode = "JP",
                        isCertified = false
                    )
                ),
                addresses = listOf(AddressDto("New Address", "WORK"))
            )

            every { userRepository.findUserById(userId) } returns existingUser
            every { userRepository.save(any<User>()) } answers { firstArg<User>() }

            // When
            val updatedUser = userService.updateUser(userId, updateDto)

            // Then
            assertEquals("New Name", updatedUser.name)
            assertEquals(35, updatedUser.age)
            assertEquals(Gender.FEMALE.code, updatedUser.gender)
            assertEquals("new@example.com", updatedUser.email)
            assertEquals(1, updatedUser.phones.size)
            assertEquals("+87654321", updatedUser.phones[0].number)
            assertEquals(1, updatedUser.addresses.size)
            assertEquals("New Address", updatedUser.addresses[0].address)
            assertEquals("JP", updatedUser.phones[0].countryCode)
        }

        @Test
        fun `updateUser - Only some values are changed`() {
            // Given
            val userId = 1L
            val existingUser = User(
                id = userId,
                name = "Old Name",
                age = 30,
                gender = Gender.MALE,
                email = "old@example.com",
                phones = mutableListOf(Phone(
                    label = Label.WORK,
                    number = "+821012345678",
                    countryCode = "KR",
                    isCertified = true
                )),
                addresses = mutableListOf(Address(label = Label.HOME, address = "Old Address"))
            )

            val updateDto = UserReqDto(
                name = "Updated Name", // 변경
            )

            every { userRepository.findUserById(userId) } returns existingUser
            every { userRepository.save(any<User>()) } answers { firstArg<User>() }

            // When
            val updatedUser = userService.updateUser(userId, updateDto)

            // Then
            assertEquals("Updated Name", updatedUser.name)  // 변경된 값 확인
            assertEquals(30, updatedUser.age) // 기존 값 유지 확인
            assertEquals(Gender.MALE.code, updatedUser.gender)  // 기존 값 유지 확인
            assertEquals("old@example.com", updatedUser.email) // 기존 값 유지 확인
            assertEquals(1, updatedUser.phones.size) // 기존 값 유지 확인
            assertEquals("+821012345678", updatedUser.phones[0].number)  // 기존 값 유지 확인
            assertEquals(1, updatedUser.addresses.size)  // 기존 값 유지 확인
            assertEquals("Old Address", updatedUser.addresses[0].address)  // 기존 값 유지 확인
        }

        @Test
        fun `updateUser - should throw exception when user not found`() {
            // Given
            val userId = 1L
            val reqDto = UserReqDto(
                name = "Jane Doe",
                age = 25,
                gender = "FEMALE",
                email = "jane.doe@example.com"
            )
            every { userRepository.findUserById(userId) } returns null

            // When & Then
            val exception = assertThrows<EntityNotFoundException> {
                userService.updateUser(userId, reqDto)
            }

            assertEquals(ErrorCode.USER_NOT_FOUND, exception.errorCode)
            verify(exactly = 1) { userRepository.findUserById(userId) }
            verify(exactly = 0) { userRepository.save(any()) }
        }
    }

    @Nested
    inner class DeleteUserTests {
        @Test
        fun `deleteUser - successfully delete a user`() {
            // Given
            val userId = 1L
            val user = mockk<User>() // Mock된 User 객체 생성

            // Repository 동작 Mock
            every { userRepository.findById(userId) } returns java.util.Optional.of(user)
            every { userRepository.delete(user) } just runs // delete()는 void 반환

            // When
            userService.deleteUser(userId)

            // Then
            verify(exactly = 1) { userRepository.findById(userId) } // findById 호출 확인
            verify(exactly = 1) { userRepository.delete(user) } // delete 호출 확인
        }

        @Test
        fun `deleteUser - should throw exception when user not found`() {
            // Given
            val userId = 1L

            // Repository 동작 Mock (사용자가 없을 경우)
            every { userRepository.findById(userId) } returns java.util.Optional.empty()

            // When & Then
            val exception = assertThrows<EntityNotFoundException> {
                userService.deleteUser(userId)
            }

            // 예외 메시지 및 ErrorCode 검증
            assert(exception.errorCode == ErrorCode.USER_NOT_FOUND)

            verify(exactly = 1) { userRepository.findById(userId) } // findById 호출 확인
            verify(exactly = 0) { userRepository.delete(any()) } // delete 호출되지 않음 확인
        }
    }
}