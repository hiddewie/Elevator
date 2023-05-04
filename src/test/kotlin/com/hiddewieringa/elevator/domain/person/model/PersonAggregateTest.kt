package com.hiddewieringa.elevator.domain.person.model

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId
import com.hiddewieringa.elevator.domain.person.EnterElevator
import com.hiddewieringa.elevator.domain.person.LeaveElevator
import com.hiddewieringa.elevator.domain.person.PersonArrived
import com.hiddewieringa.elevator.domain.person.PersonArrives
import com.hiddewieringa.elevator.domain.person.PersonEnteredElevator
import com.hiddewieringa.elevator.domain.person.PersonLeftElevator
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.Before
import org.junit.Test
import java.util.UUID

class PersonAggregateTest {

    private lateinit var fixture: FixtureConfiguration<PersonAggregate>

    @Before
    fun setUp() {
        fixture = AggregateTestFixture(PersonAggregate::class.java)
    }

    @Test
    fun create() {
        val personId = PersonId(UUID.randomUUID())
        fixture.given()
            .`when`(PersonArrives(personId, 0, 1))
            .expectEvents(PersonArrived(personId, 0, 1))
    }

    @Test
    fun enter() {
        val elevatorId = ElevatorId(UUID.randomUUID())
        val personId = PersonId(UUID.randomUUID())
        fixture.given(PersonArrived(personId, 0, 1))
            .`when`(EnterElevator(personId, elevatorId))
            .expectEvents(PersonEnteredElevator(personId, elevatorId))
    }

    @Test
    fun enter2() {
        val elevatorId = ElevatorId(UUID.randomUUID())
        val personId = PersonId(UUID.randomUUID())
        fixture.given(
            PersonArrived(personId, 0, 1),
            PersonEnteredElevator(personId, elevatorId),
        )
            .`when`(EnterElevator(personId, elevatorId))
            .expectException(IllegalArgumentException::class.java)
    }

    @Test
    fun enterLeave() {
        val elevatorId = ElevatorId(UUID.randomUUID())
        val personId = PersonId(UUID.randomUUID())
        fixture.given(
            PersonArrived(personId, 0, 1),
            PersonEnteredElevator(personId, elevatorId),
        )
            .`when`(LeaveElevator(personId, elevatorId))
            .expectEvents(PersonLeftElevator(personId, elevatorId))
    }

    @Test
    fun leave2() {
        val elevatorId = ElevatorId(UUID.randomUUID())
        val personId = PersonId(UUID.randomUUID())
        fixture.given(PersonArrived(personId, 0, 1))
            .`when`(LeaveElevator(personId, elevatorId))
            .expectException(IllegalArgumentException::class.java)
    }
}
