package com.hiddewieringa.elevator.projection.entity.person

import org.hibernate.annotations.Type
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "person")
data class Person(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Type(type = "org.hibernate.type.UUIDBinaryType")
    @Column(length = 16)
    var uuid: UUID,

    var inElevator: Boolean = false,

    var initialFloor: Long
)
