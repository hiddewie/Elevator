package com.hiddewieringa.elevator.domain.elevator

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorDirection
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorGroupId
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

class ActiveQuery
@JsonAutoDetect
data class FloorResult(val floor: Long?)

class QueryFloor(
    @field:TargetAggregateIdentifier
    val elevatorId: ElevatorId
)
