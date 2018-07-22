package com.hiddewieringa.elevator.domain.elevator.model;

import com.hiddewieringa.elevator.domain.elevator.ElevatorCreated;
import com.hiddewieringa.elevator.domain.person.*;
import com.hiddewieringa.elevator.domain.person.model.PersonAggregate;
import com.hiddewieringa.elevator.domain.person.model.PersonId;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class PersonAggregateTest {

    private FixtureConfiguration<PersonAggregate> fixture;

    @Before
    public void setUp() {
        fixture = new AggregateTestFixture<>(PersonAggregate.class);
    }

    @Test
    public void create() {
        PersonId personId = new PersonId(UUID.randomUUID());
        fixture.given()
                .when(new PersonArrives(personId, 0, 1))
                .expectEvents(new PersonArrived(personId, 0, 1));
    }

    @Test
    public void enter() {
        PersonId personId = new PersonId(UUID.randomUUID());
        fixture.given(new PersonArrived(personId, 0, 1))
                .when(new EnterElevator(personId))
                .expectEvents(new PersonEnteredElevator(personId));
    }

    @Test
    public void enter2() {
        PersonId personId = new PersonId(UUID.randomUUID());
        fixture.given(new PersonArrived(personId, 0, 1),
                new PersonEnteredElevator(personId))
                .when(new EnterElevator(personId))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    public void enterLeave() {
        PersonId personId = new PersonId(UUID.randomUUID());
        fixture.given(new PersonArrived(personId, 0, 1),
                new PersonEnteredElevator(personId))
                .when(new LeaveElevator(personId))
                .expectEvents(new PersonLeftElevator(personId));
    }

    @Test
    public void leave2() {
        PersonId personId = new PersonId(UUID.randomUUID());
        fixture.given(new PersonArrived(personId, 0, 1))
                .when(new LeaveElevator(personId))
                .expectException(IllegalArgumentException.class);
    }
}
