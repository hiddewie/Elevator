package com.hiddewieringa.elevator.domain.elevator.model

import com.hiddewieringa.elevator.domain.elevator.AssignElevatorTarget
import com.hiddewieringa.elevator.domain.elevator.CloseDoors
import com.hiddewieringa.elevator.domain.elevator.CreateElevator
import com.hiddewieringa.elevator.domain.elevator.ElevatorCreated
import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsClosed
import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsOpened
import com.hiddewieringa.elevator.domain.elevator.ElevatorMovedToFloor
import com.hiddewieringa.elevator.domain.elevator.ElevatorTargetAssigned
import com.hiddewieringa.elevator.domain.elevator.ElevatorTargetRemoved
import com.hiddewieringa.elevator.domain.elevator.OpenDoors
import com.hiddewieringa.elevator.domain.elevator.RemoveElevatorTarget
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.spring.stereotype.Aggregate
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(ElevatorAggregate::class.java)

@Aggregate
class ElevatorAggregate {

    @AggregateIdentifier
    private lateinit var id: ElevatorId
    private lateinit var groupId: ElevatorGroupId

    private var floor = 0L
    private var doorsOpened = false
    private var targets = mutableSetOf<Long>()

    constructor()

    @CommandHandler
    constructor(command: CreateElevator) {
        apply(ElevatorCreated(command.elevatorId, command.groupId, command.initalFloor, command.initialDirection))
    }

    @EventSourcingHandler
    fun handle(event: ElevatorCreated) {
        this.id = event.elevatorId
        this.groupId = event.groupId
        this.floor = event.initialFloor
    }

    @CommandHandler
    fun handle(command: OpenDoors) {
        if (doorsOpened) {
            logger.warn("Doors cannot be opened for ${id.id}, already open")
            return
        }
        apply(ElevatorDoorsOpened(command.elevatorId))
    }

    @EventSourcingHandler
    fun handle(event: ElevatorDoorsOpened) {
        doorsOpened = true
    }

    @CommandHandler
    fun handle(command: CloseDoors) {
        if (!doorsOpened) {
            logger.warn("Doors cannot be closed for ${id.id}, already closed")
            return
        }
        apply(ElevatorDoorsClosed(command.elevatorId))
    }

    @EventSourcingHandler
    fun handle(event: ElevatorDoorsClosed) {
        doorsOpened = false
    }

    @EventSourcingHandler
    fun handle(event: ElevatorMovedToFloor) {
        floor = event.floor
    }

    @CommandHandler
    fun handle(command: AssignElevatorTarget) {
        if (targets.contains(command.floor)) {
            logger.warn("Target is already in targets for ${id.id}")
            return
        }

        apply(ElevatorTargetAssigned(command.elevatorId, command.floor))
    }

    @EventSourcingHandler
    fun handle(event: ElevatorTargetAssigned) {
        targets.add(event.floor)
    }

    @CommandHandler
    fun handle(command: RemoveElevatorTarget) {
        if (!targets.contains(command.floor)) {
            logger.warn("Target is not in targets for ${id.id}")
            return
        }

        apply(ElevatorTargetRemoved(command.elevatorId, command.floor))
    }

    @EventSourcingHandler
    fun handle(event: ElevatorTargetRemoved) {
        targets.remove(event.floor)
    }
}
