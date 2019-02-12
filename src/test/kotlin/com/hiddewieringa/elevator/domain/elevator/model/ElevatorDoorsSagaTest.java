package com.hiddewieringa.elevator.domain.elevator.model;

import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsClosed;
import com.hiddewieringa.elevator.domain.elevator.ElevatorDoorsOpened;
import com.hiddewieringa.elevator.domain.saga.ElevatorDoorsSaga;
import org.axonframework.test.saga.SagaTestFixture;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.UUID;

public class ElevatorDoorsSagaTest {

    private SagaTestFixture<ElevatorDoorsSaga> fixture;

    @Before
    public void setUp() {
        fixture = new SagaTestFixture<>(ElevatorDoorsSaga.class);
    }

    @Test
    public void open() {
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());

        fixture.givenNoPriorActivity()
                .whenPublishingA(new ElevatorDoorsOpened(elevatorId))
                .expectActiveSagas(1)
                .expectAssociationWith("elevatorId", elevatorId.toString())
                .expectScheduledEvent(Duration.ofSeconds(10), new ElevatorDoorsClosed(elevatorId))
                .expectNoDispatchedCommands();
    }

    @Test
    public void close() {
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());

        fixture.givenAPublished(new ElevatorDoorsOpened(elevatorId))
                .whenPublishingA(new ElevatorDoorsClosed(elevatorId))
                .expectActiveSagas(0)
                .expectNoScheduledEvents()
                .expectNoDispatchedCommands();
    }

    @Test
    public void autoClose() {
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());

        fixture.givenAPublished(new ElevatorDoorsOpened(elevatorId))
                .whenTimeElapses(Duration.ofSeconds(11))
                .expectNoScheduledEvents()
                .expectActiveSagas(0)
                .expectNoDispatchedCommands();
    }

}
