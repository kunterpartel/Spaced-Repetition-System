package ee.concise.card_service.domain.card.features

import ee.concise.card_service.domain.card.common.Card
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class CreateCardFeature(private val saveCardFeature: SaveCardFeature) {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    fun create(parameters: Parameters): Card {
        logger.info("Creating a new card with {}", parameters)

        return saveCardFeature.save(
            Card(
                0, parameters.deckId,
                parameters.question,
                parameters.answer,
                LocalDateTime.now().atZone(ZoneId.of("UTC")).toLocalDateTime(),
                5
            )
        )
    }


    data class Parameters(val deckId: Long, val answer: String, val question: String)
}
