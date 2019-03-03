package com.hiddewieringa.elevator.domain.saga

import com.hiddewieringa.elevator.domain.elevator.*
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorDirection
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.GenericEventMessage
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga

@Saga
class ElevatorTargetsSaga {

    private lateinit var elevatorId: ElevatorId
    private var floor: Long = 0
    private var targetFloors = mutableListOf<Long>()
    private var direction = ElevatorDirection.UP

    private var doorsOpened = false

    @StartSaga
    @SagaEventHandler(associationProperty = "elevatorId")
    fun created(event: ElevatorCreated) {
        elevatorId = event.elevatorId
        direction = event.initialDirection
        floor = event.initalFloor
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun targetAssigned(event: ElevatorTargetAssigned, eventBus: EventBus) {
        targetFloors.add(event.floor)

        if (!doorsOpened) {
            moveToNextFloorIfAvailableTarget(eventBus)
        }
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun targetRemoved(event: ElevatorTargetRemoved) {
        targetFloors.remove(event.floor)
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun elevatorMoved(event: ElevatorMovedToFloor, eventBus: EventBus, commandGateway: CommandGateway) {
        floor = event.floor

        targetFloors.remove(event.floor)
        commandGateway.send(OpenDoors(elevatorId), LoggingCallback.INSTANCE)
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun doorsOpened(event: ElevatorDoorsOpened, eventBus: EventBus) {
        doorsOpened = true
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun doorsClosed(event: ElevatorDoorsClosed, eventBus: EventBus) {
        doorsOpened = false
        moveToNextFloorIfAvailableTarget(eventBus)
    }

    private fun moveToNextFloorIfAvailableTarget(eventBus: EventBus) {
        if (targetFloors.isEmpty()) {
            return
        }

        val targetUp = targetFloors.filter { it > floor }.min()
        val targetDown = targetFloors.filter { it < floor }.max()

        if (direction == ElevatorDirection.UP && targetUp != null) {
            moveToFloor(targetUp, eventBus)
            return
        } else if (direction == ElevatorDirection.DOWN && targetDown != null) {
            moveToFloor(targetDown, eventBus)
            return
        } else if (targetUp != null) {
            direction = ElevatorDirection.UP
            moveToFloor(targetUp, eventBus)
        } else if (targetDown != null) {
            direction = ElevatorDirection.DOWN
            moveToFloor(targetDown, eventBus)
        }
    }

    private fun moveToFloor(target: Long, eventBus: EventBus) {
        eventBus.publish(GenericEventMessage(ElevatorMovedToFloor(elevatorId, target)))
    }

}
