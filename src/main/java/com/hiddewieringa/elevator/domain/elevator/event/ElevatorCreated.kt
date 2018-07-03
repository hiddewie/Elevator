package com.hiddewieringa.elevator.domain.elevator.event

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.commandhandling.TargetAggregateIdentifier

class ElevatorCreated(@field:TargetAggregateIdentifier
                      val elevatorId: ElevatorId)
