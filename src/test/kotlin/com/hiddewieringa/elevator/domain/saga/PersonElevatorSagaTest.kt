package com.hiddewieringa.elevator.domain.saga

import com.hiddewieringa.elevator.domain.elevator.*
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import com.hiddewieringa.elevator.domain.elevator.ActiveQuery
import com.hiddewieringa.elevator.domain.elevator.FloorResult
import com.hiddewieringa.elevator.domain.elevator.QueryFloor
import com.hiddewieringa.elevator.domain.person.*
import com.hiddewieringa.elevator.domain.person.model.PersonId
import com.hiddewieringa.elevator.projection.entity.elevator.Elevator
import org.axonframework.common.Registration
import org.axonframework.messaging.MessageDispatchInterceptor
import org.axonframework.messaging.responsetypes.ResponseType
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.QueryMessage
import org.axonframework.queryhandling.SubscriptionQueryBackpressure
import org.axonframework.queryhandling.SubscriptionQueryResult
import org.axonframework.test.saga.SagaTestFixture
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.stream.Stream

class PersonElevatorSagaTest {

    private lateinit var fixture: SagaTestFixture<PersonElevatorSaga>

    @Before
    fun setUp() {
        fixture = SagaTestFixture(PersonElevatorSaga::class.java)
    }

    private fun queryGateway(elevatorId: ElevatorId): QueryGateway {
        return object : QueryGateway {
            override fun <R, Q> query(
                queryName: String,
                query: Q,
                responseType: ResponseType<R>
            ): CompletableFuture<R> {
                if (query is ActiveQuery) {
                    return CompletableFuture.completedFuture(
                        listOf(
                            Elevator(
                                null,
                                elevatorId.id,
                                UUID.randomUUID(),
                                0,
                                0,
                                0
                            )
                        )
                    ) as CompletableFuture<R>

                } else if (query is QueryFloor) {
                    return CompletableFuture.completedFuture(FloorResult(0)) as CompletableFuture<R>

                }

                return CompletableFuture.completedFuture(null)
            }

            override fun <R, Q> scatterGather(
                queryName: String,
                query: Q,
                responseType: ResponseType<R>,
                timeout: Long,
                timeUnit: TimeUnit
            ): Stream<R>? {
                return null
            }

            override fun <Q, I, U> subscriptionQuery(
                queryName: String,
                query: Q,
                initialResponseType: ResponseType<I>,
                updateResponseType: ResponseType<U>,
                backpressure: SubscriptionQueryBackpressure,
                updateBufferSize: Int
            ): SubscriptionQueryResult<I, U>? {
                return null
            }

            override fun registerDispatchInterceptor(dispatchInterceptor: MessageDispatchInterceptor<in QueryMessage<*, *>>): Registration? {
                return null
            }
        }
    }

    @Test
    fun create() {
        val personId = PersonId(UUID.randomUUID())
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.registerResource(queryGateway(elevatorId))

        fixture.givenNoPriorActivity()
            .whenPublishingA(PersonArrived(personId, 0, 2))
            .expectActiveSagas(1)
            .expectAssociationWith("personId", personId.toString())
            .expectAssociationWith("elevatorId", elevatorId.toString())
            .expectDispatchedCommands(AssignElevatorTarget(elevatorId, 0))
    }

    @Test
    fun movingToFloor() {
        val personId = PersonId(UUID.randomUUID())
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.registerResource(queryGateway(elevatorId))

        fixture.givenAPublished(PersonArrived(personId, 0, 2))
            .whenPublishingA(ElevatorMovedToFloor(elevatorId, 1))
            .expectActiveSagas(1)
            .expectNoDispatchedCommands()
    }

    @Test
    fun movedToFloor() {
        val personId = PersonId(UUID.randomUUID())
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.registerResource(queryGateway(elevatorId))

        fixture.givenAPublished(PersonArrived(personId, 0, 2))
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 1))
            .whenPublishingA(ElevatorMovedToFloor(elevatorId, 0))
            .expectActiveSagas(1)
            .expectNoDispatchedCommands()
    }

    @Test
    fun openDoors() {
        val personId = PersonId(UUID.randomUUID())
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.registerResource(queryGateway(elevatorId))

        fixture.givenAPublished(PersonArrived(personId, 0, 2))
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 1))
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 0))
            .whenPublishingA(ElevatorDoorsOpened(elevatorId))
            .expectActiveSagas(1)
            .expectDispatchedCommands(EnterElevator(personId, elevatorId))
    }

    @Test
    fun openAndCloseDoors() {
        val personId = PersonId(UUID.randomUUID())
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.registerResource(queryGateway(elevatorId))

        fixture.givenAPublished(PersonArrived(personId, 0, 2))
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 0))
            .andThenAPublished(ElevatorDoorsOpened(elevatorId))
            .whenPublishingA(ElevatorDoorsClosed(elevatorId))
            .expectActiveSagas(1)
            .expectNoDispatchedCommands()
    }

    @Test
    fun closeDoors() {
        val personId = PersonId(UUID.randomUUID())
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.registerResource(queryGateway(elevatorId))

        fixture.givenAPublished(PersonArrived(personId, 0, 2))
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 0))
            .andThenAPublished(ElevatorDoorsOpened(elevatorId))
            .whenPublishingA(PersonEnteredElevator(personId, elevatorId))
            .expectActiveSagas(1)
            .expectDispatchedCommands(AssignElevatorTarget(elevatorId, 2), CloseDoors(elevatorId))
    }

    @Test
    fun moveElevatorWithPerson() {
        val personId = PersonId(UUID.randomUUID())
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.registerResource(queryGateway(elevatorId))

        fixture.givenAPublished(PersonArrived(personId, 0, 2))
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 0))
            .andThenAPublished(ElevatorDoorsOpened(elevatorId))
            .andThenAPublished(PersonEnteredElevator(personId, elevatorId))
            .andThenAPublished(ElevatorTargetAssigned(elevatorId, 2))
            .andThenAPublished(ElevatorDoorsClosed(elevatorId))
            .whenPublishingA(ElevatorMovedToFloor(elevatorId, 1))
            .expectActiveSagas(1)
            .expectNoDispatchedCommands()
    }

    @Test
    fun personLeaves() {
        val personId = PersonId(UUID.randomUUID())
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.registerResource(queryGateway(elevatorId))

        fixture.givenAPublished(PersonArrived(personId, 0, 2))
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 0))
            .andThenAPublished(ElevatorDoorsOpened(elevatorId))
            .andThenAPublished(PersonEnteredElevator(personId, elevatorId))
            .andThenAPublished(ElevatorTargetAssigned(elevatorId, 2))
            .andThenAPublished(ElevatorDoorsClosed(elevatorId))
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 2))
            .whenPublishingA(ElevatorDoorsOpened(elevatorId))
            .expectDispatchedCommands(LeaveElevator(personId, elevatorId))
            .expectActiveSagas(0)
    }

    @Test
    fun emptyElevatorDoorsClose() {
        val personId = PersonId(UUID.randomUUID())
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.registerResource(queryGateway(elevatorId))

        fixture.givenAPublished(PersonArrived(personId, 0, 2))
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 0))
            .andThenAPublished(ElevatorDoorsOpened(elevatorId))
            .andThenAPublished(PersonEnteredElevator(personId, elevatorId))
            .andThenAPublished(ElevatorTargetAssigned(elevatorId, 2))
            .andThenAPublished(ElevatorDoorsClosed(elevatorId))
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 2))
            .andThenAPublished(ElevatorDoorsOpened(elevatorId))
            .whenPublishingA(PersonLeftElevator(personId, elevatorId))
            .expectActiveSagas(0)
            .expectNoDispatchedCommands()
    }
}
