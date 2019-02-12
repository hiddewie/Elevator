package com.hiddewieringa.elevator.domain.elevator.model;

import com.hiddewieringa.elevator.domain.elevator.*;
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
    public void open() {
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.given(new ElevatorCreated(elevatorId))
                .when(new OpenDoors(elevatorId))
                .expectEvents(new ElevatorDoorsOpened(elevatorId));
    }

    @Test
    public void open2() {
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.given(new ElevatorCreated(elevatorId),
                new ElevatorDoorsOpened(elevatorId))
                .when(new OpenDoors(elevatorId))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    public void close() {
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.given(new ElevatorCreated(elevatorId),
                new ElevatorDoorsOpened(elevatorId))
                .when(new CloseDoors(elevatorId))
                .expectEvents(new ElevatorDoorsClosed(elevatorId));
    }

    @Test
    public void close2() {
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.given(new ElevatorCreated(elevatorId))
                .when(new CloseDoors(elevatorId))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    public void callElevator0() {
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.given(new ElevatorCreated(elevatorId))
                .when(new CallElevator(elevatorId, 0))
                .expectEvents(new ElevatorMovedToFloor(elevatorId, 0));
    }

    @Test
    public void callElevator1() {
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.given(new ElevatorCreated(elevatorId))
                .when(new CallElevator(elevatorId, 1))
                .expectEvents(new ElevatorMovedToFloor(elevatorId, 1));
    }

    @Test
    public void callElevator3() {
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.given(new ElevatorCreated(elevatorId))
                .when(new CallElevator(elevatorId, 3))
                .expectEvents(new ElevatorMovedToFloor(elevatorId, 1),
                        new ElevatorMovedToFloor(elevatorId, 2),
                        new ElevatorMovedToFloor(elevatorId, 3));
    }
}
