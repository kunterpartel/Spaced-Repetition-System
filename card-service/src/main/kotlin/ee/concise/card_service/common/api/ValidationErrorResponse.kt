package ee.concise.card_service.common.api

import ee.concise.card_service.common.exception.validation.ValidationException
import java.util.*

class ValidationErrorResponse : GenericErrorResponse {
    val errors: TreeSet<FieldErrorResponse> = TreeSet(FieldErrorResponse::compareTo)

    constructor(message: String, errors: Collection<FieldErrorResponse>) : super(message) {
        this.errors.addAll(errors)
    }

    constructor(e: ValidationException) : this(
        e.message ?: "Validation exception",
        e.errors.map { FieldErrorResponse(it) }
    )
}
