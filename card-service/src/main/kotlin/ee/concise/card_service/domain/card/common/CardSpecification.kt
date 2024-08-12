package ee.concise.card_service.domain.card.common

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime

object CardSpecification {

    fun deckIdEquals(deckId: Long?): Specification<Card?> {
        return Specification<Card?> { root: Root<Card?>, _: CriteriaQuery<*>?, builder: CriteriaBuilder ->
            builder.equal(root.get<Long>("deckId"), deckId)
        }
    }

    fun idEquals(id: Long?): Specification<Card?> {
        return Specification<Card?> { root: Root<Card?>, _: CriteriaQuery<*>?, builder: CriteriaBuilder ->
            builder.equal(root.get<Long>("id"), id)
        }
    }


    fun dueDateIsLessThan(dueDate: LocalDateTime?): Specification<Card?> {
        return Specification<Card?> { root: Root<Card?>, _: CriteriaQuery<*>?, builder: CriteriaBuilder ->
            builder.lessThanOrEqualTo(
                root.get<LocalDateTime>("dueDate"), dueDate
            )
        }
    }
}
