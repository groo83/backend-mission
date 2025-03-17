package com.murple.chat.common.valid

import com.murple.chat.common.annoation.ValidEnum
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EnumValidator : ConstraintValidator<ValidEnum, String> {

    private lateinit var annotation: ValidEnum

    override fun initialize(constraintAnnotation: ValidEnum) {
        this.annotation = constraintAnnotation
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        // null 값 체크
        if (value == null) return true

        val enumValues = annotation.target.java.enumConstants
        return enumValues.any { enumVal ->
            when (enumVal) {
                is Enum<*> -> value == enumVal.name || (annotation.ignoreCase && value.equals(enumVal.name, ignoreCase = true))
                else -> false
            }
        }
    }
}