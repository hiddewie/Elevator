package com.hiddewieringa.elevator.domain.elevator.model

import com.hiddewieringa.elevator.domain.person.*
import com.hiddewieringa.elevator.domain.person.model.PersonAggregate
import com.hiddewieringa.elevator.domain.person.model.PersonId
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.Before
import org.junit.Test
import java.util.*

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
        val personId = PersonId(UUID.randomUUID())
        fixture.given(PersonArrived(personId, 0, 1))
                .`when`(EnterElevator(personId))
                .expectEvents(PersonEnteredElevator(personId))
    }

    @Test
    fun enter2() {
        val personId = PersonId(UUID.randomUUID())
        fixture.given(PersonArrived(personId, 0, 1),
                PersonEnteredElevator(personId))
                .`when`(EnterElevator(personId))
                .expectException(IllegalArgumentException::class.java)
    }

    @Test
    fun enterLeave() {
        val personId = PersonId(UUID.randomUUID())
        fixture.given(PersonArrived(personId, 0, 1),
                PersonEnteredElevator(personId))
                .`when`(LeaveElevator(personId))
                .expectEvents(PersonLeftElevator(personId))
    }

    @Test
    fun leave2() {
        val personId = PersonId(UUID.randomUUID())
        fixture.given(PersonArrived(personId, 0, 1))
                .`when`(LeaveElevator(personId))
                .expectException(IllegalArgumentException::class.java)
    }
}
