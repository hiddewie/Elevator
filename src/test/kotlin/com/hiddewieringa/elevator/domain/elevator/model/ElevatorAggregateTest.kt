package com.hiddewieringa.elevator.domain.elevator.model

import com.hiddewieringa.elevator.domain.elevator.*
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.Before
import org.junit.Test
import java.util.*

class ElevatorAggregateTest {

    private lateinit var fixture: FixtureConfiguration<ElevatorAggregate>

    @Before
    fun setUp() {
        fixture = AggregateTestFixture(ElevatorAggregate::class.java)
    }

    @Test
    fun create() {
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.given()
                .`when`(CreateElevator(elevatorId))
                .expectEvents(ElevatorCreated(elevatorId))
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
        fixture.given(ElevatorCreated(elevatorId),
                ElevatorDoorsOpened(elevatorId))
                .`when`(OpenDoors(elevatorId))
                .expectException(IllegalArgumentException::class.java)
    }

    @Test
    fun close() {
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.given(ElevatorCreated(elevatorId),
                ElevatorDoorsOpened(elevatorId))
                .`when`(CloseDoors(elevatorId))
                .expectEvents(ElevatorDoorsClosed(elevatorId))
    }

    @Test
    fun close2() {
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.given(ElevatorCreated(elevatorId))
                .`when`(CloseDoors(elevatorId))
                .expectException(IllegalArgumentException::class.java)
    }

    @Test
    fun callElevator0() {
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.given(ElevatorCreated(elevatorId))
                .`when`(CallElevator(elevatorId, 0))
                .expectEvents(ElevatorMovedToFloor(elevatorId, 0))
    }

    @Test
    fun callElevator1() {
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.given(ElevatorCreated(elevatorId))
                .`when`(CallElevator(elevatorId, 1))
                .expectEvents(ElevatorMovedToFloor(elevatorId, 1))
    }

    @Test
    fun callElevator3() {
        val elevatorId = ElevatorId(UUID.randomUUID())
        fixture.given(ElevatorCreated(elevatorId))
                .`when`(CallElevator(elevatorId, 3))
                .expectEvents(ElevatorMovedToFloor(elevatorId, 1),
                        ElevatorMovedToFloor(elevatorId, 2),
                        ElevatorMovedToFloor(elevatorId, 3))
    }
}
