package ee.concise.card_service.common.api

import ee.concise.card_service.common.exception.persistence.Criteria


data class CriteriaResponse(
    val field: String,
    val value: String
) : Comparable<CriteriaResponse> {

    constructor(criteria: Criteria) : this(
        criteria.field,
        criteria.value
    )

    override fun compareTo(other: CriteriaResponse): Int {
        return Comparator.nullsFirst(
            Comparator.comparing(CriteriaResponse::field, NULL_SAFE_STRING_COMPARATOR)
                .thenComparing(CriteriaResponse::value, NULL_SAFE_STRING_COMPARATOR)
        ).compare(this, other)
    }

    companion object {
        private val NULL_SAFE_STRING_COMPARATOR = Comparator.nullsFirst(
            String::compareTo
        )
    }
}



