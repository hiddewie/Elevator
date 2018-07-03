package com.hiddewieringa.elevator.domain.elevator.event

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.commandhandling.TargetAggregateIdentifier

class ElevatorCalled(@field:TargetAggregateIdentifier
                     val elevatorId: ElevatorId, val floor: Long)
