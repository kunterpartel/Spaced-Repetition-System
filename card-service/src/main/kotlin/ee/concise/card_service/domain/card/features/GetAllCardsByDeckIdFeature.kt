package ee.concise.card_service.domain.card.features

import ee.concise.card_service.domain.card.common.Card
import ee.concise.card_service.domain.card.common.CardRepository
import ee.concise.card_service.domain.card.common.CardSpecification
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class GetAllCardsByDeckIdFeature(private val cardRepository: CardRepository) {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun get(deckId: Long): List<Card> {
        logger.info("Fetching all Card for deck $deckId")

        return cardRepository.findAll(
            CardSpecification.dueDateIsLessThan(LocalDateTime.now())
                .and(CardSpecification.deckIdEquals(deckId))
        )
    }
}
