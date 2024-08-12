package ee.concise.card_service.domain.card.features

import ee.concise.card_service.domain.card.common.StrategyType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ProcessSessionResultsFeature(
    private val saveCardFeature: SaveCardFeature,
    private val getCardByIdFeature: GetCardByIdFeature,
    private val schedulingStrategyFactory: SchedulingStrategyFactory
) {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun process(parameters: List<Parameter>) {
        logger.info("Processing ${parameters.joinToString(" ")}")

        parameters.forEach { result ->
            val card = getCardByIdFeature.get(result.cardId)

            val strategy = schedulingStrategyFactory.getStrategy(result.strategy)

            strategy.schedule(card, result.correct)

            card.dueDate = LocalDateTime.now().plusMinutes(card.interval)
            saveCardFeature.save(card)
        }

    }

    data class Parameter(val cardId: Long, val correct: Boolean, val strategy: StrategyType)
}
