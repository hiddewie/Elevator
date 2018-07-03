package com.hiddewieringa.elevator.domain.elevator.command

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.commandhandling.TargetAggregateIdentifier

data class OpenDoors(@field:TargetAggregateIdentifier val elevatorId: ElevatorId)
