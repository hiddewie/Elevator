package com.hiddewieringa.elevator.domain.elevator.command

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.commandhandling.TargetAggregateIdentifier

data class MoveToFloor(@field:TargetAggregateIdentifier val elevatorId: ElevatorId, val floor: Long)
