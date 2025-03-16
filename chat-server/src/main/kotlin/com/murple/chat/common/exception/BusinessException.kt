package com.murple.chat.common.exception

import com.murple.chat.common.code.ErrorCode

open class BusinessException(
    val errorCode: ErrorCode,
    message: String = errorCode.message
) : RuntimeException(message)

class DuplicateResourceException(errorCode: ErrorCode) :
    BusinessException(errorCode)

class EntityNotFoundException(errorCode: ErrorCode) :
    BusinessException(errorCode)

class InvalidValueException(errorCode: ErrorCode) :
    BusinessException(errorCode)
