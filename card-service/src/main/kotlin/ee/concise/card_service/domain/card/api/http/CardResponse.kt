package ee.concise.card_service.domain.card.api.http

import java.time.LocalDateTime

data class CardResponse(
    val id: Long,
    val deckId: Long,
    val dueDate: LocalDateTime,
    val question: String,
    val answer: String
) {
}
