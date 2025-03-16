package com.murple.chat.domain.user.api

import com.murple.chat.common.dto.DataResponse
import com.murple.chat.domain.user.application.UserService
import com.murple.chat.domain.user.dto.UserReqDto
import com.murple.chat.domain.user.dto.UserResDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController (private val userService: UserService){

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): DataResponse<UserResDto> {
        return DataResponse.create(userService.getUserById(id))
    }

    @PostMapping
    fun createUser(@RequestBody reqDto: UserReqDto): DataResponse<UserResDto>  {
        return DataResponse.create(userService.createUser(reqDto))
    }

}