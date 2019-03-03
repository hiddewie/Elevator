package com.hiddewieringa.elevator.domain.person

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import com.hiddewieringa.elevator.domain.person.model.PersonId
import org.axonframework.modelling.command.TargetAggregateIdentifier

class PersonArrived(@field:TargetAggregateIdentifier val personId: PersonId, val floor: Long, val requestedFloor: Long)

class PersonEnteredElevator(
    @field:TargetAggregateIdentifier
    val personId: PersonId,
    val elevatorId: ElevatorId
)

class PersonLeftElevator(
    @field:TargetAggregateIdentifier
    val personId: PersonId,
    val elevatorId: ElevatorId
)
