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
import kotlin.math.abs

@Saga
class ElevatorTargetsSaga {

    lateinit var elevatorId: ElevatorId
    var floor: Long = 0
    var targetFloors = mutableSetOf<Long>()
    var direction = ElevatorDirection.UP
    var doorsOpened = false
    var underway = false

    @StartSaga
    @SagaEventHandler(associationProperty = "elevatorId")
    fun created(event: ElevatorCreated) {
        elevatorId = event.elevatorId
        direction = event.initialDirection
        floor = event.initialFloor
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun targetAssigned(event: ElevatorTargetAssigned, scheduler: EventScheduler) {
        targetFloors.add(event.floor)

        if (!doorsOpened) {
            moveToNextFloorIfAvailableTarget(event.elevatorId, scheduler)
        }
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun targetRemoved(event: ElevatorTargetRemoved) {
        targetFloors.remove(event.floor)
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun elevatorMoved(event: ElevatorMovedToFloor, commandGateway: CommandGateway) {
        floor = event.floor
        if (targetFloors.contains(event.floor)) {
            underway = false

            commandGateway.send(RemoveElevatorTarget(elevatorId, floor), LoggingCallback.INSTANCE)
            commandGateway.send(OpenDoors(elevatorId), LoggingCallback.INSTANCE)
        }
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun doorsOpened(event: ElevatorDoorsOpened) {
        doorsOpened = true
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun doorsClosed(event: ElevatorDoorsClosed, scheduler: EventScheduler) {
        doorsOpened = false
        moveToNextFloorIfAvailableTarget(event.elevatorId, scheduler)
    }

    private fun moveToNextFloorIfAvailableTarget(
        elevatorId: ElevatorId,
        scheduler: EventScheduler
    ) {
        if (targetFloors.isEmpty() || underway) {
            return
        }

        val currentFloorIsTarget = targetFloors.contains(floor)
        val targetUp = targetFloors.filter { it > floor }.min()
        val targetDown = targetFloors.filter { it < floor }.max()

        if (currentFloorIsTarget) {
            moveToFloor(elevatorId, floor, scheduler)
        } else if (direction == ElevatorDirection.UP && targetUp != null) {
            moveToFloor(elevatorId, targetUp, scheduler)
        } else if (direction == ElevatorDirection.DOWN && targetDown != null) {
            moveToFloor(elevatorId, targetDown, scheduler)
        } else if (targetUp != null) {
            direction = ElevatorDirection.UP
            moveToFloor(elevatorId, targetUp, scheduler)
        } else if (targetDown != null) {
            direction = ElevatorDirection.DOWN
            moveToFloor(elevatorId, targetDown, scheduler)
        }
    }

    private fun moveToFloor(elevatorId: ElevatorId, target: Long, scheduler: EventScheduler) {
        underway = true
        if (floor == target) {
            scheduler.schedule(Duration.ofSeconds(1), ElevatorMovedToFloor(elevatorId, target))
        } else {
            val floors = (if (floor < target) (floor..target) else (floor downTo target))
                .drop(1)

            floors.forEach {
                scheduler.schedule(Duration.ofSeconds(1 + abs(it - floor)), ElevatorMovedToFloor(elevatorId, it))
            }
        }
    }

}
