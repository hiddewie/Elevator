package com.hiddewieringa.elevator.domain.saga

import com.hiddewieringa.elevator.domain.elevator.*
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import com.hiddewieringa.elevator.domain.person.*
import com.hiddewieringa.elevator.domain.person.model.PersonId
import com.hiddewieringa.elevator.query.ElevatorQueryService
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.saga.SagaEventHandler
import org.axonframework.eventhandling.saga.SagaLifecycle
import org.axonframework.eventhandling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired

@Saga
class PersonElevatorSaga {

    @Autowired
    @Transient
    private lateinit var commandGateway: CommandGateway

    @Autowired
    @Transient
    private lateinit var elevatorQueryService: ElevatorQueryService

    private lateinit var personId: PersonId
    private lateinit var elevatorId: ElevatorId

    private var personFloor = 0L
    private var requestedFloor = 0L
    private var elevatorFloor = 0L

    private var personInElevator = false

    @StartSaga
    @SagaEventHandler(associationProperty = "personId")
    fun start(event: PersonArrived) {
        personId = event.personId
        println("Person arrived, using elevator ${elevatorQueryService.active().first()}")
        elevatorId = ElevatorId(elevatorQueryService.active().first().id)
        SagaLifecycle.associateWith("elevatorId", elevatorId.toString())

        personFloor = event.floor
        requestedFloor = event.requestedFloor
        commandGateway.send(CallElevator(elevatorId, event.floor), LoggingCallback.INSTANCE);
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun floor(event: ElevatorMovedToFloor) {
        elevatorFloor = event.floor
        if (event.floor == personFloor || event.floor == requestedFloor) {
            commandGateway.send(OpenDoors(elevatorId), LoggingCallback.INSTANCE)
        }
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun opened(event: ElevatorDoorsOpened) {
        if (elevatorFloor == personFloor) {
            commandGateway.send(EnterElevator(personId), LoggingCallback.INSTANCE)
        } else if (elevatorFloor == requestedFloor) {
            commandGateway.send(LeaveElevator(personId), LoggingCallback.INSTANCE)
        }
    }

    @SagaEventHandler(associationProperty = "personId")
    fun entered(event: PersonEnteredElevator) {
        commandGateway.send(CloseDoors(elevatorId), LoggingCallback.INSTANCE)
        personInElevator = true
    }

    @SagaEventHandler(associationProperty = "personId")
    fun entered(event: PersonLeftElevator) {
        commandGateway.send(CloseDoors(elevatorId), LoggingCallback.INSTANCE)
        personInElevator = false
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun entered(event: ElevatorDoorsClosed) {
        if (personInElevator) {
            commandGateway.send(CallElevator(elevatorId, requestedFloor), LoggingCallback.INSTANCE)
        } else {
            SagaLifecycle.end()
        }
    }

}
