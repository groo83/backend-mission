package com.murple.chat.domain.user.api

import com.murple.chat.common.dto.DataResponse
import com.murple.chat.domain.user.application.UserService
import com.murple.chat.domain.user.dto.UserReqDto
import com.murple.chat.domain.user.dto.UserResDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController (private val userService: UserService){

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): DataResponse<UserResDto> {
        return DataResponse.create(userService.getUserById(id))
    }

    @PostMapping
    fun createUser(@RequestBody reqDto: UserReqDto): DataResponse<UserResDto>  {
        return DataResponse.create(userService.createUser(reqDto))
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody reqDto: UserReqDto): DataResponse<UserResDto> {
        return DataResponse.create(userService.updateUser(id, reqDto))
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): DataResponse<Long> {
        userService.deleteUser(id)
        return DataResponse.create(id)
    }
}