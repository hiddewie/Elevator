package com.hiddewieringa.elevator.domain.saga

import com.hiddewieringa.elevator.domain.elevator.ElevatorCreated
import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsClosed
import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsOpened
import com.hiddewieringa.elevator.domain.elevator.ElevatorMovedToFloor
import com.hiddewieringa.elevator.domain.elevator.ElevatorTargetAssigned
import com.hiddewieringa.elevator.domain.elevator.ElevatorTargetRemoved
import com.hiddewieringa.elevator.domain.elevator.OpenDoors
import com.hiddewieringa.elevator.domain.elevator.RemoveElevatorTarget
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorDirection
import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import org.axonframework.test.saga.SagaTestFixture
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.util.UUID

class ElevatorTargetsSagaTest {

    private lateinit var fixture: SagaTestFixture<ElevatorTargetsSaga>

    @Before
    fun setUp() {
        fixture = SagaTestFixture(ElevatorTargetsSaga::class.java)
    }

    @Test
    fun created() {
        val elevatorId = ElevatorId(UUID.randomUUID())

        fixture.givenNoPriorActivity()
            .whenPublishingA(ElevatorCreated(elevatorId))
            .expectActiveSagas(1)
            .expectAssociationWith("elevatorId", elevatorId.toString())
            .expectNoDispatchedCommands()
    }

    @Test
    fun targetAssigned() {
        val elevatorId = ElevatorId(UUID.randomUUID())

        fixture.givenAPublished(ElevatorCreated(elevatorId))
            .whenPublishingA(ElevatorTargetAssigned(elevatorId, 1))
            .expectActiveSagas(1)
            .expectNoDispatchedCommands()
            .expectScheduledEvent(Duration.ofSeconds(2), ElevatorMovedToFloor(elevatorId, 1))
    }

    @Test
    fun elevatorMovedAndTargetRemoved() {
        val elevatorId = ElevatorId(UUID.randomUUID())

        fixture.givenAPublished(ElevatorCreated(elevatorId))
            .andThenAPublished(ElevatorTargetAssigned(elevatorId, 1))
            .whenPublishingA(ElevatorMovedToFloor(elevatorId, 1))
            .expectActiveSagas(1)
            .expectDispatchedCommands(RemoveElevatorTarget(elevatorId, 1), OpenDoors(elevatorId))
            .expectPublishedEvents()
    }

    @Test
    fun assignedWithDoorsOpened() {
        val elevatorId = ElevatorId(UUID.randomUUID())

        fixture.givenAPublished(ElevatorCreated(elevatorId))
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 0))
            .andThenAPublished(ElevatorDoorsOpened(elevatorId))
            .whenPublishingA(ElevatorTargetAssigned(elevatorId, 3))
            .expectActiveSagas(1)
            .expectNoDispatchedCommands()
            .expectPublishedEvents()
    }

    @Test
    fun multipleAssignedWithDoorsOpened() {
        val elevatorId = ElevatorId(UUID.randomUUID())

        fixture.givenAPublished(ElevatorCreated(elevatorId))
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 0))
            .andThenAPublished(ElevatorDoorsOpened(elevatorId))
            .andThenAPublished(ElevatorTargetAssigned(elevatorId, 2))
            .whenPublishingA(ElevatorTargetAssigned(elevatorId, 3))
            .expectActiveSagas(1)
            .expectNoDispatchedCommands()
            .expectPublishedEvents()
    }

    @Test
    fun targetRemoved() {
        val elevatorId = ElevatorId(UUID.randomUUID())

        fixture.givenAPublished(ElevatorCreated(elevatorId))
            .whenPublishingA(ElevatorTargetRemoved(elevatorId, 1))
            .expectActiveSagas(1)
            .expectNoDispatchedCommands()
            .expectNoScheduledEvents()
    }

    @Test
    fun doorsCloseNoTargets() {
        val elevatorId = ElevatorId(UUID.randomUUID())

        fixture.givenAPublished(ElevatorCreated(elevatorId))
            .andThenAPublished(ElevatorDoorsOpened(elevatorId))
            .whenPublishingA(ElevatorDoorsClosed(elevatorId))
            .expectActiveSagas(1)
            .expectNoDispatchedCommands()
            .expectNoScheduledEvents()
    }

    @Test
    fun moveOnDoorsClose() {
        val elevatorId = ElevatorId(UUID.randomUUID())

        fixture.givenAPublished(ElevatorCreated(elevatorId, initialFloor = 10))
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 5))
            .andThenAPublished(ElevatorDoorsOpened(elevatorId))
            .andThenAPublished(ElevatorTargetAssigned(elevatorId, 3))
            .andThenAPublished(ElevatorTargetAssigned(elevatorId, 2))
            .andThenAPublished(ElevatorTargetAssigned(elevatorId, 1))
            .whenPublishingA(ElevatorDoorsClosed(elevatorId))
            .expectActiveSagas(1)
            .expectNoDispatchedCommands()
            .expectScheduledEvent(Duration.ofSeconds(3), ElevatorMovedToFloor(elevatorId, 3))
    }

    @Test
    fun firstMoveInDirectionDown() {
        val elevatorId = ElevatorId(UUID.randomUUID())

        fixture.givenAPublished(
            ElevatorCreated(
                elevatorId,
                initialFloor = 10,
                initialDirection = ElevatorDirection.DOWN
            )
        )
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 5))
            .andThenAPublished(ElevatorDoorsOpened(elevatorId))
            .andThenAPublished(ElevatorTargetAssigned(elevatorId, 6))
            .andThenAPublished(ElevatorTargetAssigned(elevatorId, 4))
            .whenPublishingA(ElevatorDoorsClosed(elevatorId))
            .expectActiveSagas(1)
            .expectNoDispatchedCommands()
            .expectScheduledEvent(Duration.ofSeconds(2), ElevatorMovedToFloor(elevatorId, 4))
    }

    @Test
    fun firstMoveInDirectionUp() {
        val elevatorId = ElevatorId(UUID.randomUUID())

        fixture.givenAPublished(ElevatorCreated(elevatorId, initialFloor = 10, initialDirection = ElevatorDirection.UP))
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 5))
            .andThenAPublished(ElevatorDoorsOpened(elevatorId))
            .andThenAPublished(ElevatorTargetAssigned(elevatorId, 6))
            .andThenAPublished(ElevatorTargetAssigned(elevatorId, 4))
            .whenPublishingA(ElevatorDoorsClosed(elevatorId))
            .expectActiveSagas(1)
            .expectNoDispatchedCommands()
            .expectScheduledEvent(Duration.ofSeconds(2), ElevatorMovedToFloor(elevatorId, 6))
    }

    @Test
    fun firstMoveInDirectionUpThenDown() {
        val elevatorId = ElevatorId(UUID.randomUUID())

        fixture.givenAPublished(ElevatorCreated(elevatorId, initialFloor = 10, initialDirection = ElevatorDirection.UP))
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 5))
            .andThenAPublished(ElevatorDoorsOpened(elevatorId))
            .andThenAPublished(ElevatorTargetAssigned(elevatorId, 6))
            .andThenAPublished(ElevatorTargetAssigned(elevatorId, 4))
            .andThenAPublished(ElevatorDoorsClosed(elevatorId))
            .andThenAPublished(ElevatorMovedToFloor(elevatorId, 6))
            .andThenAPublished(ElevatorTargetRemoved(elevatorId, 6))
            .andThenAPublished(ElevatorDoorsOpened(elevatorId))
            .whenPublishingA(ElevatorDoorsClosed(elevatorId))
            .expectActiveSagas(1)
            .expectNoDispatchedCommands()
            .expectScheduledEvent(Duration.ofSeconds(3), ElevatorMovedToFloor(elevatorId, 4))
    }

    @Test
    fun elevatorMoved() {
        val elevatorId = ElevatorId(UUID.randomUUID())

        fixture.givenAPublished(ElevatorCreated(elevatorId, initialFloor = 10))
            .whenPublishingA(ElevatorTargetAssigned(elevatorId, 0))
            .expectActiveSagas(1)
            .expectNoDispatchedCommands()
            .expectScheduledEvent(Duration.ofSeconds(11), ElevatorMovedToFloor(elevatorId, 0))
    }
}
