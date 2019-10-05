package com.hiddewieringa.elevator.domain.saga

import com.hiddewieringa.elevator.domain.elevator.AssignElevatorTarget
import com.hiddewieringa.elevator.domain.elevator.CloseDoors
import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsOpened
import com.hiddewieringa.elevator.domain.elevator.ElevatorMovedToFloor
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import com.hiddewieringa.elevator.domain.elevator.ActiveQuery
import com.hiddewieringa.elevator.domain.person.*
import com.hiddewieringa.elevator.domain.person.model.PersonId
import com.hiddewieringa.elevator.projection.entity.elevator.Elevator
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.SagaLifecycle
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.spring.stereotype.Saga

@Saga
class PersonElevatorSaga {

    lateinit var personId: PersonId
    lateinit var elevatorId: ElevatorId

    var personFloor = 0L
    var requestedFloor = 0L
    var elevatorFloor = 0L

    var personInElevator = false

    @StartSaga
    @SagaEventHandler(associationProperty = "personId")
    fun start(event: PersonArrived, queryGateway: QueryGateway, commandGateway: CommandGateway) {
        personId = event.personId
        val elevator = queryGateway.query(ActiveQuery(), ResponseTypes.multipleInstancesOf(Elevator::class.java)).get()
            .let(this::elevatorWithLeastTargets)
            ?: throw RuntimeException("No elevator found for arrival of person $personId")

        println("Person arrived, using elevator $elevator")
        elevatorId = ElevatorId(elevator.uuid)
        SagaLifecycle.associateWith("elevatorId", elevatorId.toString())

        personFloor = event.floor
        requestedFloor = event.requestedFloor
        commandGateway.send(AssignElevatorTarget(elevatorId, event.floor), LoggingCallback.INSTANCE);
    }

    private fun elevatorWithLeastTargets(elevators: List<Elevator>): Elevator? {
        return elevators.minBy { it.numberOfTargets }
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun floor(event: ElevatorMovedToFloor) {
        elevatorFloor = event.floor
    }

    @SagaEventHandler(associationProperty = "elevatorId")
    fun opened(event: ElevatorDoorsOpened, commandGateway: CommandGateway) {
        if (elevatorFloor == personFloor) {
            commandGateway.send(EnterElevator(personId, event.elevatorId), LoggingCallback.INSTANCE)
        } else if (elevatorFloor == requestedFloor) {
            commandGateway.send(LeaveElevator(personId, event.elevatorId), LoggingCallback.INSTANCE)
            SagaLifecycle.end()
        }
    }

    @SagaEventHandler(associationProperty = "personId")
    fun entered(event: PersonEnteredElevator, commandGateway: CommandGateway) {
        commandGateway.send(AssignElevatorTarget(elevatorId, requestedFloor), LoggingCallback.INSTANCE)
        commandGateway.send(CloseDoors(elevatorId), LoggingCallback.INSTANCE)
        personInElevator = true
    }

    @SagaEventHandler(associationProperty = "personId")
    fun entered(event: PersonLeftElevator) {
        personInElevator = false
    }

}
