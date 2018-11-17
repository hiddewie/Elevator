package com.hiddewieringa.elevator.projection.entity.elevator

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Elevator(
        @Id
        val id: UUID,

        val containsPerson: Boolean
) {
        override fun toString(): String {
                return "Elevator(id=$id, containsPerson=$containsPerson)"
        }
}
