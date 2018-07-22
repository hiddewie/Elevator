package com.hiddewieringa.elevator.domain.elevator

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.commandhandling.TargetAggregateIdentifier

data class ElevatorDoorsOpened(@field:TargetAggregateIdentifier val elevatorId: ElevatorId)

data class ElevatorDoorsClosed(@field:TargetAggregateIdentifier val elevatorId: ElevatorId)

data class ElevatorCreated(@field:TargetAggregateIdentifier val elevatorId: ElevatorId)

data class ElevatorMovedToFloor(@field:TargetAggregateIdentifier val elevatorId: ElevatorId, val floor: Long)
