package com.murple.chat.common.code

import org.springframework.http.HttpStatus

enum class ErrorCode (val status: Int, val code: String, val message: String){
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "C001", "사용자 정보가 없습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST.value(), "M001", "파라미터가 유효하지 않습니다."),
    INVALID_PARAMETER_PHONE_NUMBER(HttpStatus.BAD_REQUEST.value(), "M002", "전화번호는 E.164 형식(+국가코드와 숫자)이어야 합니다. (e.g. +821012345678)"),
    IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "I001", "입출력 오류가 발생했습니다."),
}