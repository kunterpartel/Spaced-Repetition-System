package ee.concise.card_service.domain.card.features

import ee.concise.card_service.domain.card.common.Card
import ee.concise.card_service.domain.card.common.SchedulingStrategy
import ee.concise.card_service.domain.card.common.StrategyType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SchedulingStrategyFactory {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    fun getStrategy(strategyType: StrategyType): SchedulingStrategy {
        return when (strategyType) {
            StrategyType.BASIC -> BasicSchedulingStrategy()
            StrategyType.LEITNER -> LeitnerSchedulingStrategy()
        }
    }

    inner class BasicSchedulingStrategy : SchedulingStrategy {
        override fun schedule(card: Card, correct: Boolean): Card {
            logger.info("Applying BasicSchedulingStrategy")

            if (correct) {
                card.interval *= 2
            } else {
                card.interval = 5L
            }
            card.dueDate = LocalDateTime.now().plusMinutes(card.interval)
            return card
        }
    }

    inner class LeitnerSchedulingStrategy : SchedulingStrategy {
        override fun schedule(card: Card, correct: Boolean): Card {
            logger.info("Applying LeitnerSchedulingStrategy")
            if (correct) {
                card.interval *= 2
            } else {
                card.interval = 5L
            }
            card.dueDate = LocalDateTime.now().plusMinutes(card.interval)
            return card
        }
    }
}
