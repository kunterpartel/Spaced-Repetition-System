package ee.concise.card_service.domain.card.features

import ee.concise.card_service.domain.card.common.Card
import ee.concise.card_service.domain.card.common.CardRepository
import ee.concise.card_service.domain.card.common.CardSpecification
import jakarta.persistence.EntityNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GetCardByIdFeature(private val repository: CardRepository) {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun get(id: Long): Card {
        logger.info("Fetching card by id: $id")

        return repository.findOne(CardSpecification.idEquals(id)).orElseThrow { EntityNotFoundException("Card with id $id not found") }
    }
}
