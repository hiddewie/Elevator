package com.hiddewieringa.elevator.domain.elevator.query

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.modelling.command.TargetAggregateIdentifier

class QueryFloor(
    @field:TargetAggregateIdentifier
    val elevatorId: ElevatorId
)
