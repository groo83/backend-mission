package com.murple.chat.common.annoation

import com.murple.chat.common.valid.EnumValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [EnumValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidEnum(
    val message: String = "Invalid value. This is not permitted.",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val target: KClass<out Enum<*>>,
    val ignoreCase: Boolean = false
)
