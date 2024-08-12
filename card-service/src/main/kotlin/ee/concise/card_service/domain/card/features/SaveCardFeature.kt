package ee.concise.card_service.domain.card.features

import ee.concise.card_service.domain.card.common.Card
import ee.concise.card_service.domain.card.common.CardRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SaveCardFeature(private val repository: CardRepository) {

    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    fun save(card: Card): Card {
        logger.info("Saving card")

        return repository.save(card)
    }
}
