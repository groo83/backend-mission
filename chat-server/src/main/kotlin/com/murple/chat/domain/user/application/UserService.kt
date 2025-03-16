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
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService (private val userRepository: UserRepository){
    fun getUserById(id: Long): UserResDto {
        val user = userRepository.findUserById(id)
            ?: throw EntityNotFoundException(ErrorCode.USER_NOT_FOUND)
        return UserResDto.from(user)
    }

    @Transactional
    fun createUser(reqDto: UserReqDto): UserResDto {
        val user = User(
            name = reqDto.name,
            age = reqDto.age,
            gender = reqDto.gender?.let { Gender.of(it) },
            email = reqDto.email
        )

        // Phone 리스트 생성 후 User 설정
        user.phones = reqDto.phones?.map { dto ->
            Phone(
                label = Label.of(dto.label),
                number = dto.number,
                countryCode = dto.countryCode,
                isCertified = dto.isCertified,
                user = user // ★ User 설정 필수 ★
            )
        }?.toMutableList() ?: mutableListOf()

        // Address 리스트 생성 후 User 설정
        user.addresses = reqDto.addresses?.map { dto ->
            Address(
                label = Label.of(dto.label),
                address = dto.address,
                user = user // ★ User 설정 필수 ★
            )
        }?.toMutableList() ?: mutableListOf()

        val saveUser = userRepository.save(user)
        return UserResDto.from(saveUser)
    }

}
