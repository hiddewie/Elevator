package com.hiddewieringa.elevator.domain.saga

import com.hiddewieringa.elevator.domain.elevator.*
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorDirection
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.scheduling.EventScheduler
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import java.time.Duration

@Saga
class ElevatorTargetsSaga {

    private lateinit var elevatorId: ElevatorId
    private var floor: Long = 0
    private var targetFloors = mutableListOf<Long>()
    private var direction = ElevatorDirection.UP

    private var doorsOpened = false

    private var underway = false

    @StartSaga
    @SagaEventHandler(associationProperty = "elevatorId")
    fun created(event: ElevatorCreated) {
        elevatorId = event.elevatorId
        direction = event.initialDirection
        floor = event.initalFloor
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun targetAssigned(event: ElevatorTargetAssigned, scheduler: EventScheduler) {
        targetFloors.add(event.floor)

        if (!doorsOpened) {
            moveToNextFloorIfAvailableTarget(scheduler)
        }
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun targetRemoved(event: ElevatorTargetRemoved) {
        targetFloors.remove(event.floor)
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun elevatorMoved(event: ElevatorMovedToFloor, commandGateway: CommandGateway) {
        floor = event.floor
        underway = false

        targetFloors.remove(event.floor)
        commandGateway.send(OpenDoors(elevatorId), LoggingCallback.INSTANCE)
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun doorsOpened(event: ElevatorDoorsOpened) {
        doorsOpened = true
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun doorsClosed(event: ElevatorDoorsClosed, scheduler: EventScheduler) {
        doorsOpened = false
        moveToNextFloorIfAvailableTarget(scheduler)
    }

    private fun moveToNextFloorIfAvailableTarget(scheduler: EventScheduler) {
        if (targetFloors.isEmpty() || underway) {
            return
        }

        val targetUp = targetFloors.filter { it > floor }.min()
        val targetDown = targetFloors.filter { it < floor }.max()

        if (direction == ElevatorDirection.UP && targetUp != null) {
            moveToFloor(targetUp, scheduler)
            return
        } else if (direction == ElevatorDirection.DOWN && targetDown != null) {
            moveToFloor(targetDown, scheduler)
            return
        } else if (targetUp != null) {
            direction = ElevatorDirection.UP
            moveToFloor(targetUp, scheduler)
        } else if (targetDown != null) {
            direction = ElevatorDirection.DOWN
            moveToFloor(targetDown, scheduler)
        }
    }

    private fun moveToFloor(target: Long, scheduler: EventScheduler) {
        scheduler.schedule(Duration.ofSeconds(Math.abs(floor - target)), ElevatorMovedToFloor(elevatorId, target))
        underway = true
    }

}
