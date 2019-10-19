package com.hiddewieringa.elevator.projection.entity.elevator

import org.hibernate.annotations.Type
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "elevator")
data class Elevator(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Type(type = "org.hibernate.type.UUIDBinaryType")
    @Column(length = 16)
    var uuid: UUID,

    @Type(type = "org.hibernate.type.UUIDBinaryType")
    @Column(name = "elevatorGroup", length = 16)
    var group: UUID,

    var floor: Long,

    var numberOfPersons: Int = 0,
    var numberOfTargets: Int = 0,

    var doorsOpened: Boolean = false
)
