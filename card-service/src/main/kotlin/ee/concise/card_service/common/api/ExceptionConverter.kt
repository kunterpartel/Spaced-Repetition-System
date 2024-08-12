package ee.concise.card_service.common.api

import ee.concise.card_service.common.exception.validation.FieldError
import ee.concise.card_service.common.exception.validation.ValidationException
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import org.springframework.validation.BindingResult

object ExceptionConverter {

    const val CONSTRAINT_VIOLATION_MESSAGE = "CONSTRAINT_VIOLATION"

    fun convert(e: ConstraintViolationException): ValidationException {
        val fieldErrors = e.constraintViolations.map { error ->
            FieldError(
                getFieldName(error),
                getValidatorName(error),
                error.message
            )
        }.toSet()

        return ValidationException(CONSTRAINT_VIOLATION_MESSAGE, fieldErrors)
    }

    fun translateBindingResult(bindingResult: BindingResult): ValidationException {
        val fieldErrors = mutableSetOf<FieldError>()

        fieldErrors.addAll(bindingResult.fieldErrors.map { error ->
            FieldError(
                error.field,
                error.codes?.get(0)?.split(".")!!.get(0),
                parseMessage(error)
            )
        })

        fieldErrors.addAll(bindingResult.globalErrors.map { error ->
            FieldError(
                error.objectName,
                error.codes?.get(0)?.split(".")!!.get(0),
                error.defaultMessage!!
            )
        })

        return ValidationException("INVALID_ARGUMENT", fieldErrors)
    }

    private fun getFieldName(error: ConstraintViolation<*>): String {
        val parts = error.propertyPath.toString().split(".")
        return parts.last()
    }

    private fun getValidatorName(constraintViolation: ConstraintViolation<*>): String {
        val validatorName = constraintViolation.constraintDescriptor.annotation.annotationClass.simpleName
        val parts = validatorName!!.split(".")
        return parts.last()
    }

    private fun parseMessage(error: org.springframework.validation.FieldError): String {
        return if (error.isBindingFailure) {
            if (error.rejectedValue != null) {
                "Unable to interpret value: ${error.rejectedValue}"
            } else {
                "Unable to interpret value"
            }
        } else {
            error.defaultMessage ?: ""
        }
    }
}
