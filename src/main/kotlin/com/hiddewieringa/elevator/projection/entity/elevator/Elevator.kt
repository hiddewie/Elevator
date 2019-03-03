package com.hiddewieringa.elevator.projection.entity.elevator

import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "elevator")
class Elevator(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Type(type = "org.hibernate.type.UUIDBinaryType")
    @Column(length = 16)
    var uuid: UUID,

    @Type(type = "org.hibernate.type.UUIDBinaryType")
    @Column(length = 16)
    var group: UUID,

    var floor: Long,

    var numberOfPersons: Int = 0,
    var numberOfTargets: Int = 0
)
