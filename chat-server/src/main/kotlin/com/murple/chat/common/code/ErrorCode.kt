package com.murple.chat.common.code

import org.springframework.http.HttpStatus

enum class ErrorCode (val status: Int, val code: String, val message: String){
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "C001", "사용자 정보가 없습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST.value(), "M004", "파라미터가 유효하지 않습니다."),
    IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "I001", "입출력 오류가 발생했습니다."),
}