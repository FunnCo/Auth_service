package com.crowtest.auth_service.entity

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.util.*

@Entity
@Table(name = "group", schema = "public")
class Group (
        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(
                name = "UUID",
                strategy = "org.hibernate.id.UUIDGenerator"
        )
        @Column(name = "group_id", nullable = false)
        val group_id: UUID? = null,

        @Column(name = "name")
        val name: String
)