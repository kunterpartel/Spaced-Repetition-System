package ee.concise.card_service.common.api

import ee.concise.card_service.common.exception.validation.FieldError


data class FieldErrorResponse(
    val field: String,
    val reason: String,
    val message: String
) : Comparable<FieldErrorResponse> {

    constructor(e: FieldError) : this(e.field, e.reason, e.message)

    companion object {
        private val NULL_SAFE_STRING_COMPARATOR: Comparator<String> = Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER)
    }

    override fun compareTo(other: FieldErrorResponse): Int {
        return Comparator.nullsFirst(
            Comparator.comparing(FieldErrorResponse::field, NULL_SAFE_STRING_COMPARATOR)
                .thenComparing(FieldErrorResponse::reason, NULL_SAFE_STRING_COMPARATOR)
                .thenComparing(FieldErrorResponse::message, NULL_SAFE_STRING_COMPARATOR)
        ).compare(this, other)
    }
}
