package com.hiddewieringa.elevator.domain.elevator.command

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.commandhandling.TargetAggregateIdentifier

data class CloseDoors(@field:TargetAggregateIdentifier val elevatorId: ElevatorId)
