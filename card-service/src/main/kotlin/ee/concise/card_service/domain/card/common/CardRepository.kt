package ee.concise.card_service.domain.card.common

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface CardRepository : JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {
}
