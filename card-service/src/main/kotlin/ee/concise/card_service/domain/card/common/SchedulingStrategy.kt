package ee.concise.card_service.domain.card.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

interface SchedulingStrategy {
    fun schedule(card: Card, correct: Boolean): Card
}

class LeitnerSchedulingStrategyFeature : SchedulingStrategy {
    companion object {
        private val INTERVALS = arrayOf(1, 3, 7, 15, 30) // Example intervals in days
    }

    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun schedule(card: Card, correct: Boolean): Card {
        logger.info("Applying LeitnerSchedulingStrategy")
        if (correct) {
            // Move to the next box if not already in the last box
            val nextBox = (card.boxNumber + 1).coerceAtMost(INTERVALS.size - 1)
            card.boxNumber = nextBox
        } else {
            // Reset to the first box
            card.boxNumber = 0
        }
        // Update the due date based on the current box
        card.dueDate = LocalDateTime.now().plusDays(INTERVALS[card.boxNumber].toLong())

        return card
    }
}

class BasicSchedulingStrategyFeature : SchedulingStrategy {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

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
