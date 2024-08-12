package ee.concise.card_service.domain.card.api.http

import ee.concise.card_service.domain.card.common.StrategyType

data class SessionResult(val cardId: Long, val correct: Boolean)

data class SessionResultsRequest(
    val results: List<SessionResult>,
    val strategyType: StrategyType
)
