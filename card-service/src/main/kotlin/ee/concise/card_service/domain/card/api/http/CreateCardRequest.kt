package ee.concise.card_service.domain.card.api.http

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty

data class CreateCardRequest(
    @field:Min(1)
    val deckId: Long = 0,
    @field:NotEmpty
    val answer: String = "",
    @field:NotEmpty
    val question: String = ""
) {
}
