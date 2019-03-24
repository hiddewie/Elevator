package com.hiddewieringa.elevator.domain.person

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import com.hiddewieringa.elevator.domain.person.model.PersonId
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class PersonArrived(
    @field:TargetAggregateIdentifier val personId: PersonId,
    val floor: Long,
    val requestedFloor: Long
)

data class PersonEnteredElevator(
    @field:TargetAggregateIdentifier
    val personId: PersonId,
    val elevatorId: ElevatorId
)

data class PersonLeftElevator(
    @field:TargetAggregateIdentifier
    val personId: PersonId,
    val elevatorId: ElevatorId
)
