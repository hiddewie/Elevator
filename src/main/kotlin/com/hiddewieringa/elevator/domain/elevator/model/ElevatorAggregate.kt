package com.hiddewieringa.elevator.domain.elevator.model

import com.hiddewieringa.elevator.domain.elevator.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.EventHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class ElevatorAggregate {

    @AggregateIdentifier
    private lateinit var id: ElevatorId
    private lateinit var groupId: ElevatorGroupId

    private var floor = 0L

    private var doorsOpened = false

    constructor()

    @CommandHandler
    constructor(command: CreateElevator) {
        apply(ElevatorCreated(command.elevatorId, command.groupId, command.initalFloor, command.initialDirection))
    }

    @EventHandler
    fun handle(event: ElevatorCreated) {
        this.id = event.elevatorId
        this.groupId = event.groupId
        this.floor = event.initalFloor
    }

    @CommandHandler
    fun handle(command: OpenDoors) {
        if (doorsOpened) {
            return
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
            return
        }
        apply(ElevatorDoorsClosed(command.elevatorId))
    }

    @EventHandler
    fun handle(event: ElevatorDoorsClosed) {
        doorsOpened = false
    }

    @EventHandler
    fun handle(event: ElevatorMovedToFloor) {
        floor = event.floor
    }

    @CommandHandler
    fun handle(event: AssignElevatorTarget) {
        apply(ElevatorTargetAssigned(event.elevatorId, event.floor))
    }
}
