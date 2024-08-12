package ee.concise.card_service.common.exception.persistence

import java.io.Serializable

data class Criteria(
    val field: String,
    val value: String
) : Serializable
