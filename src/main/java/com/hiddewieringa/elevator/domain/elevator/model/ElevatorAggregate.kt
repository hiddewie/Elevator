package com.hiddewieringa.elevator.domain.elevator.model

import com.hiddewieringa.elevator.domain.elevator.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle.apply
import org.axonframework.eventhandling.EventHandler
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class ElevatorAggregate {

    @AggregateIdentifier
    private var id: ElevatorId? = null

    private var floor = 0L

    private var doorsOpened = false

    private constructor()

    @CommandHandler
    constructor(command: CreateElevator) {
        apply(ElevatorCreated(command.elevatorId))
    }

    @EventHandler
    fun handle(event: ElevatorCreated) {
        this.id = event.elevatorId
    }

    @CommandHandler
    fun handle(command: OpenDoors) {
        if (doorsOpened) {
            throw IllegalArgumentException("Doors already open")
        }
        apply(ElevatorDoorsOpened(command.elevatorId))
    }

    @EventHandler
    fun handle(event: ElevatorDoorsOpened) {
        doorsOpened = true
    }

    @CommandHandler
    fun handle(command: CloseDoors) {
        if (!doorsOpened) {
            throw IllegalArgumentException("Doors already closed")
        }
        apply(ElevatorDoorsClosed(command.elevatorId))
    }

    @EventHandler
    fun handle(event: ElevatorDoorsClosed) {
        doorsOpened = false
    }

    @CommandHandler
    fun handle(command: CallElevator) {
        if (floor == command.floor) {
            // Floor is same as calling floor
            apply(ElevatorMovedToFloor(command.elevatorId, floor))
            return;
        }
        while (floor != command.floor) {
            apply(ElevatorMovedToFloor(command.elevatorId, if (floor > command.floor) floor - 1 else floor + 1))
        }
    }

    @EventHandler
    fun handle(event: ElevatorMovedToFloor) {
        floor = event.floor
    }
}
