package com.hiddewieringa.elevator.domain.elevator

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.commandhandling.TargetAggregateIdentifier

data class CallElevator(@field:TargetAggregateIdentifier val elevatorId: ElevatorId, val floor: Long)

data class CloseDoors(@field:TargetAggregateIdentifier val elevatorId: ElevatorId)

data class CreateElevator(@field:TargetAggregateIdentifier val elevatorId: ElevatorId)

data class OpenDoors(@field:TargetAggregateIdentifier val elevatorId: ElevatorId)
