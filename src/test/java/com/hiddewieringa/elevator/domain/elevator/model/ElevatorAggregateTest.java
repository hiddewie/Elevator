package com.hiddewieringa.elevator.domain.elevator.model;

import com.hiddewieringa.elevator.domain.elevator.command.CallElevator;
import com.hiddewieringa.elevator.domain.elevator.command.CreateElevator;
import com.hiddewieringa.elevator.domain.elevator.event.ElevatorCalled;
import com.hiddewieringa.elevator.domain.elevator.event.ElevatorCreated;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class ElevatorAggregateTest {

    private FixtureConfiguration<ElevatorAggregate> fixture;

    @Before
    public void setUp() {
        fixture = new AggregateTestFixture<>(ElevatorAggregate.class);
    }

    @Test
    public void create() {
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.given()
                .when(new CreateElevator(elevatorId))
                .expectEvents(new ElevatorCreated(elevatorId));
    }

    @Test
    public void call() {
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.given(new ElevatorCreated(elevatorId))
                .when(new CallElevator(elevatorId, floor))
                .expectEvents(new ElevatorCalled(elevatorId));
    }
}
