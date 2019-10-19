package com.hiddewieringa.elevator.domain.elevator

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.modelling.command.TargetAggregateIdentifier

class ActiveQuery
@JsonAutoDetect
data class FloorResult(val floor: Long?)

class QueryFloor(
    @field:TargetAggregateIdentifier
    val elevatorId: ElevatorId
)
