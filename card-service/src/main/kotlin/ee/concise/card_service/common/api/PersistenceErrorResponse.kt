package ee.concise.card_service.common.api

import ee.concise.card_service.common.exception.persistence.Criteria
import ee.concise.card_service.common.exception.persistence.PersistenceException
import java.util.*

class PersistenceErrorResponse(
    val entity: String,
    val criteria: TreeSet<CriteriaResponse> = TreeSet(CriteriaResponse::compareTo),
    message: String,
) : GenericErrorResponse(message) {

    constructor(message: String, entity: String, criteria: Set<Criteria>) : this(
        entity,
        criteria.map { CriteriaResponse(it) }.toCollection(TreeSet(CriteriaResponse::compareTo)),
        message
    )

    constructor(id: String, e: PersistenceException) : this(
        e.message ?: "Persistence exception",
        e.entity,
        e.criteria
    )
}

