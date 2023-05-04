package com.hiddewieringa.elevator.projection.entity.person

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "person")
data class Person(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Column(length = 16)
    var uuid: UUID,

    var inElevator: Boolean = false,

    var initialFloor: Long
)
