package com.hiddewieringa.elevator.domain.elevator.model

import com.hiddewieringa.elevator.domain.elevator.AssignElevatorTarget
import com.hiddewieringa.elevator.domain.elevator.CloseDoors
import com.hiddewieringa.elevator.domain.elevator.CreateElevator
import com.hiddewieringa.elevator.domain.elevator.ElevatorCreated
import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsClosed
import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsOpened
import com.hiddewieringa.elevator.domain.elevator.ElevatorTargetAssigned
import com.hiddewieringa.elevator.domain.elevator.OpenDoors
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.Before
import org.junit.Test
import java.util.UUID

class ElevatorAggregateTest {

    private lateinit var fixture: FixtureConfiguration<ElevatorAggregate>

    @Before
    fun setUp() {
        fixture = AggregateTestFixture(ElevatorAggregate::class.java)
    }

    @Test
    fun create() {
        val elevatorId = ElevatorId(UUID.randomUUID())
        val event = CreateElevator(elevatorId)
        fixture.given()
            .`when`(event)
            .expectEvents(ElevatorCreated(elevatorId, event.groupId, 0, ElevatorDirection.UP))
    }

    @Test
    fun open() {
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.given(ElevatorCreated(elevatorId))
            .`when`(OpenDoors(elevatorId))
            .expectEvents(ElevatorDoorsOpened(elevatorId))
    }

    @Test
    fun open2() {
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.given(
            ElevatorCreated(elevatorId),
            ElevatorDoorsOpened(elevatorId),
        )
            .`when`(OpenDoors(elevatorId))
            .expectNoEvents()
    }

    @Test
    fun close() {
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.given(
            ElevatorCreated(elevatorId),
            ElevatorDoorsOpened(elevatorId),
        )
            .`when`(CloseDoors(elevatorId))
            .expectEvents(ElevatorDoorsClosed(elevatorId))
    }

    @Test
    fun close2() {
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.given(ElevatorCreated(elevatorId))
            .`when`(CloseDoors(elevatorId))
            .expectNoEvents()
    }

    @Test
    fun assign() {
        val elevatorId = ElevatorId(UUID.randomUUID())
        val floor = 10L
        fixture.given(ElevatorCreated(elevatorId))
            .`when`(AssignElevatorTarget(elevatorId, floor))
            .expectEvents(ElevatorTargetAssigned(elevatorId, floor))
    }
}
