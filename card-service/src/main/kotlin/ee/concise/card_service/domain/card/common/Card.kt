package ee.concise.card_service.domain.card.common

import ee.concise.card_service.common.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table
class Card(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    @Column
    var deckId: Long,
    @Column
    val question: String,
    @Column
    val answer: String,
    @Column
    var dueDate: LocalDateTime,
    @Column
    var interval: Long,
    @Column(nullable = false)
    var boxNumber: Int = 0,
) : BaseEntity()

enum class StrategyType {
    BASIC, LEITNER // Add more strategies as needed
}

