package com.hiddewieringa.elevator.domain.saga

import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsAutoClosed
import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsClosed
import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsOpened
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.test.saga.SagaTestFixture
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.util.UUID

class ElevatorDoorsSagaTest {

    private lateinit var fixture: SagaTestFixture<ElevatorDoorsSaga>

    @Before
    fun setUp() {
        fixture = SagaTestFixture(ElevatorDoorsSaga::class.java)
    }

    @Test
    fun open() {
        val elevatorId = ElevatorId(UUID.randomUUID())

        fixture.givenNoPriorActivity()
            .whenPublishingA(ElevatorDoorsOpened(elevatorId))
            .expectActiveSagas(1)
            .expectAssociationWith("elevatorId", elevatorId.toString())
            .expectScheduledEvent(Duration.ofSeconds(3), ElevatorDoorsAutoClosed(elevatorId))
            .expectNoDispatchedCommands()
    }

    @Test
    fun close() {
        val elevatorId = ElevatorId(UUID.randomUUID())

        fixture.givenAPublished(ElevatorDoorsOpened(elevatorId))
            .whenPublishingA(ElevatorDoorsClosed(elevatorId))
            .expectActiveSagas(0)
            .expectNoScheduledEvents()
            .expectNoDispatchedCommands()
    }

    @Test
    fun autoClose() {
        val elevatorId = ElevatorId(UUID.randomUUID())

        fixture.givenAPublished(ElevatorDoorsOpened(elevatorId))
            .whenTimeElapses(Duration.ofSeconds(4))
            .expectNoScheduledEvents()
            .expectActiveSagas(0)
            .expectNoDispatchedCommands()
    }
}
