package com.hiddewieringa.elevator.projection.entity.elevator

import java.util.UUID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "elevator")
data class Elevator(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Column(length = 16)
    var uuid: UUID,

    @Column(name = "elevatorGroup", length = 16)
    var group: UUID,

    var floor: Long,

    var numberOfPersons: Int = 0,
    var numberOfTargets: Int = 0,

    var doorsOpened: Boolean = false
)
