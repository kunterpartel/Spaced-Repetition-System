package ee.concise.card_service.domain.card.api.http

import ee.concise.card_service.domain.card.common.Card
import ee.concise.card_service.domain.card.common.StrategyType
import ee.concise.card_service.domain.card.features.ProcessSessionResultsFeature

object CardMapper {

    fun toResponse(card: Card) = CardResponse(
        id = card.id,
        deckId = card.deckId,
        dueDate = card.dueDate,
        question = card.question,
        answer = card.answer
    )

    fun toResponseList(cards: List<Card>): List<CardResponse> {
        return cards.map { toResponse(it) }
    }

    fun toProcessSessionParameter(request: List<SessionResult>, strategy: StrategyType): List<ProcessSessionResultsFeature.Parameter> {
        return request.map { sessionRequest ->
            ProcessSessionResultsFeature.Parameter(sessionRequest.cardId, sessionRequest.correct, strategy)
        }
    }
}
