package com.hiddewieringa.elevator.domain.elevator

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorDirection
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorGroupId
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class CreateElevator(
    @field:TargetAggregateIdentifier val elevatorId: ElevatorId,
    val groupId: ElevatorGroupId = ElevatorGroupId(UUID.randomUUID()),
    val initalFloor: Long = 0L,
    val initialDirection: ElevatorDirection = ElevatorDirection.UP
)

data class AssignElevatorTarget(
    @field:TargetAggregateIdentifier val elevatorId: ElevatorId,
    val floor: Long
)

data class OpenDoors(@field:TargetAggregateIdentifier val elevatorId: ElevatorId)
data class CloseDoors(@field:TargetAggregateIdentifier val elevatorId: ElevatorId)
