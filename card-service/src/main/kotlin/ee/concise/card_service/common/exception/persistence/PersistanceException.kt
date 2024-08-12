package ee.concise.card_service.common.exception.persistence

import ee.concise.common.exception.CoreException

abstract class PersistenceException : CoreException {
    val entity: String
    val criteria: Set<Criteria>

    protected constructor(message: String?, entity: String, field: String, value: String) : this(
        message,
        entity,
        setOf(Criteria(field, value))
    )

    protected constructor(message: String?, entity: String, criteria: Set<Criteria>) : super(
        message.takeIf { it?.isNotBlank() == true }
            ?: "Exception with entity $entity where $criteria"
    ) {
        this.entity = entity
        this.criteria = criteria
    }

    abstract fun getCode(): Int
}

