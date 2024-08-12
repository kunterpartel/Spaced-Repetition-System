package ee.concise.card_service.common.exception.validation


data class FieldError(
    val field: String,
    val reason: String,
    val message: String
)

