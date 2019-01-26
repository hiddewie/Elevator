package com.hiddewieringa.elevator.domain.saga

import com.hiddewieringa.elevator.domain.elevator.*
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import com.hiddewieringa.elevator.domain.elevator.query.ActiveQuery
import com.hiddewieringa.elevator.domain.person.*
import com.hiddewieringa.elevator.domain.person.model.PersonId
import com.hiddewieringa.elevator.projection.entity.elevator.Elevator
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.saga.SagaEventHandler
import org.axonframework.eventhandling.saga.SagaLifecycle
import org.axonframework.eventhandling.saga.StartSaga
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.responsetypes.ResponseTypes
import org.axonframework.spring.stereotype.Saga

@Saga
class PersonElevatorSaga {

    private lateinit var personId: PersonId
    private lateinit var elevatorId: ElevatorId

    private var personFloor = 0L
    private var requestedFloor = 0L
    private var elevatorFloor = 0L

    private var personInElevator = false

    @StartSaga
    @SagaEventHandler(associationProperty = "personId")
    fun start(event: PersonArrived, queryGateway: QueryGateway, commandGateway: CommandGateway) {
        personId = event.personId
        val elevator = queryGateway.query(ActiveQuery(), ResponseTypes.multipleInstancesOf(Elevator::class.java)).get().first()
        println("Person arrived, using elevator ${elevator}")
        elevatorId = ElevatorId(elevator.uuid)
        SagaLifecycle.associateWith("elevatorId", elevatorId.toString())

        personFloor = event.floor
        requestedFloor = event.requestedFloor
        commandGateway.send(CallElevator(elevatorId, event.floor), LoggingCallback.INSTANCE);
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun floor(event: ElevatorMovedToFloor, commandGateway: CommandGateway) {
        elevatorFloor = event.floor
        if (event.floor == personFloor || event.floor == requestedFloor) {
            commandGateway.send(OpenDoors(elevatorId), LoggingCallback.INSTANCE)
        }
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun opened(event: ElevatorDoorsOpened, commandGateway: CommandGateway) {
        if (elevatorFloor == personFloor) {
            commandGateway.send(EnterElevator(personId), LoggingCallback.INSTANCE)
        } else if (elevatorFloor == requestedFloor) {
            commandGateway.send(LeaveElevator(personId), LoggingCallback.INSTANCE)
        }
    }

    @SagaEventHandler(associationProperty = "personId")
    fun entered(event: PersonEnteredElevator, commandGateway: CommandGateway) {
        commandGateway.send(CloseDoors(elevatorId), LoggingCallback.INSTANCE)
        personInElevator = true
    }

    @SagaEventHandler(associationProperty = "personId")
    fun entered(event: PersonLeftElevator, commandGateway: CommandGateway) {
        commandGateway.send(CloseDoors(elevatorId), LoggingCallback.INSTANCE)
        personInElevator = false
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun entered(event: ElevatorDoorsClosed, commandGateway: CommandGateway) {
        if (personInElevator) {
            commandGateway.send(CallElevator(elevatorId, requestedFloor), LoggingCallback.INSTANCE)
        } else {
            SagaLifecycle.end()
        }
    }

}
