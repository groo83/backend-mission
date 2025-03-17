package com.murple.chat.domain.user.application

import com.murple.chat.common.code.ErrorCode
import com.murple.chat.common.exception.EntityNotFoundException
import com.murple.chat.domain.user.Address
import com.murple.chat.domain.user.Phone
import com.murple.chat.domain.user.User
import com.murple.chat.domain.user.dto.UserReqDto
import com.murple.chat.domain.user.dto.UserResDto
import com.murple.chat.domain.user.enums.Gender
import com.murple.chat.domain.user.enums.Label
import com.murple.chat.domain.user.infrastructure.UserRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService (private val userRepository: UserRepository) {
    private val logger = KotlinLogging.logger {}

    fun getUserById(id: Long): UserResDto {
        val user = userRepository.findUserById(id)
            ?: throw EntityNotFoundException(ErrorCode.USER_NOT_FOUND)
        return UserResDto.from(user)
    }

    @Transactional
    fun createUser(reqDto: UserReqDto): UserResDto {
        val user = User.from(reqDto)

        val saveUser = userRepository.save(user)
        return UserResDto.from(saveUser)
    }

    @Transactional
    fun updateUser(id: Long, reqDto: UserReqDto): UserResDto {
        val findUser = userRepository.findUserById(id)
            ?: throw EntityNotFoundException(ErrorCode.USER_NOT_FOUND)

        findUser.update(
            reqDto.name,
            reqDto.age?:findUser.age,
            reqDto.gender?.let { Gender.of(it) } ?: findUser.gender,
            reqDto.email?:findUser.email,
            reqDto.phones?.map { dto ->
                    Phone(
                        label = Label.of(dto.label),
                        number = dto.number,
                        countryCode = dto.countryCode,
                        isCertified = dto.isCertified,
                        user = findUser
                    )
                }?.toMutableList() ?: findUser.phones,
            reqDto.addresses?.map { dto ->
                Address(
                    label = Label.of(dto.label),
                    address = dto.address,
                    user = findUser
                )
            }?.toMutableList() ?: findUser.addresses
        )

        val updateUser = userRepository.saveAndFlush(findUser) // commit 전 적용으로 updatedAt 컬럼 데이터 적용
        return UserResDto.from(updateUser)

    }

    @Transactional
    fun deleteUser(id: Long) {
        val user = userRepository.findById(id).orElseThrow { EntityNotFoundException(ErrorCode.USER_NOT_FOUND) }
        userRepository.delete(user)
    }

    fun findUsersByNamesOrderByCreatedAtAsc(names: List<String>): List<User> {
        val users = userRepository.findUsersByNamesOrderByCreatedAtAsc(names)

        if (users.isEmpty()) {
            logger.info { "조회된 사용자가 없습니다. 이름 목록: $names" }
        }
        return users
    }
}