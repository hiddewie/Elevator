package com.hiddewieringa.elevator.domain.elevator

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorDirection
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorGroupId
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class ElevatorDoorsOpened(@field:TargetAggregateIdentifier val elevatorId: ElevatorId)

data class ElevatorDoorsClosed(@field:TargetAggregateIdentifier val elevatorId: ElevatorId)

data class ElevatorCreated(
    @field:TargetAggregateIdentifier val elevatorId: ElevatorId,
    val groupId: ElevatorGroupId = ElevatorGroupId(UUID.randomUUID()),
    val initalFloor: Long = 0L,
    val initialDirection: ElevatorDirection = ElevatorDirection.UP
)

data class ElevatorMovedToFloor(@field:TargetAggregateIdentifier val elevatorId: ElevatorId, val floor: Long)

data class ElevatorTargetAssigned(@field:TargetAggregateIdentifier val elevatorId: ElevatorId, val floor: Long)
data class ElevatorTargetRemoved(@field:TargetAggregateIdentifier val elevatorId: ElevatorId, val floor: Long)
