package com.hiddewieringa.elevator.projection.entity.person

import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

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
