package ee.concise.card_service.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PreUpdate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {

    @Column(nullable = false, updatable = false)
    val dateCreated: LocalDateTime = LocalDateTime.now();

    @Column
    var dateModified: LocalDateTime? = null;

    @Column
    val dateDeleted: LocalDateTime? = null;

    @PreUpdate
    fun preUpdate() {
        updateDateModified();
    }

    private fun updateDateModified() {
        dateModified = LocalDateTime.now();
    }
}
