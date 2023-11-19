package com.yourssu.assignmentblog.global.common.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseCreateAndUpdateTimeEntity {
    @CreatedDate
    @Column(nullable = false, name = "created_at")
    protected var createdAt: LocalDate? = null

    @LastModifiedDate
    @Column(nullable = false, name = "updated_at")
    protected var updatedAt: LocalDate? = null
}