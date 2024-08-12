package ee.concise.card_service.common.exception.validation

import ee.concise.common.exception.CoreException

class ValidationException(
    message: String = "",
    val errors: Set<FieldError>
) : CoreException(message)
