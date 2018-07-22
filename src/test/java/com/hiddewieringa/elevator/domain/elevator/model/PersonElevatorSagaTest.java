package com.hiddewieringa.elevator.domain.elevator.model;

import com.hiddewieringa.elevator.domain.elevator.*;
import com.hiddewieringa.elevator.domain.elevator.query.ElevatorQueryHandler;
import com.hiddewieringa.elevator.domain.person.*;
import com.hiddewieringa.elevator.domain.person.model.PersonId;
import com.hiddewieringa.elevator.domain.saga.PersonElevatorSaga;
import org.axonframework.test.saga.SagaTestFixture;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PersonElevatorSagaTest {

    private SagaTestFixture<PersonElevatorSaga> fixture;

    @Before
    public void setUp() {
        fixture = new SagaTestFixture<>(PersonElevatorSaga.class);
    }

    private ElevatorQueryHandler elevatorQueryHandler(ElevatorId elevatorId) {
        return new ElevatorQueryHandler() {
            @NotNull
            public List<ElevatorId> active() {
                return Collections.singletonList(elevatorId);
            }
        };
    }

    @Test
    public void create() {
        PersonId personId = new PersonId(UUID.randomUUID());
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.registerResource(elevatorQueryHandler(elevatorId));

        fixture.givenNoPriorActivity()
                .whenPublishingA(new PersonArrived(personId, 0, 2))
                .expectActiveSagas(1)
                .expectAssociationWith("personId", personId.toString())
                .expectAssociationWith("elevatorId", elevatorId.toString())
                .expectDispatchedCommands(new CallElevator(elevatorId, 0));
    }

    @Test
    public void movingToFloor() {
        PersonId personId = new PersonId(UUID.randomUUID());
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.registerResource(elevatorQueryHandler(elevatorId));

        fixture.givenAPublished(new PersonArrived(personId, 0, 2))
                .whenPublishingA(new ElevatorMovedToFloor(elevatorId, 1))
                .expectActiveSagas(1)
                .expectNoDispatchedCommands();
    }

    @Test
    public void movedToFloor() throws Exception {
        PersonId personId = new PersonId(UUID.randomUUID());
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.registerResource(elevatorQueryHandler(elevatorId));

        fixture.givenAPublished(new PersonArrived(personId, 0, 2))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 1))
                .whenPublishingA(new ElevatorMovedToFloor(elevatorId, 0))
                .expectActiveSagas(1)
                .expectDispatchedCommands(new OpenDoors(elevatorId));
    }

    @Test
    public void openDoors() throws Exception {
        PersonId personId = new PersonId(UUID.randomUUID());
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.registerResource(elevatorQueryHandler(elevatorId));

        fixture.givenAPublished(new PersonArrived(personId, 0, 2))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 1))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 0))
                .whenPublishingA(new ElevatorDoorsOpened(elevatorId))
                .expectActiveSagas(1)
                .expectDispatchedCommands(new EnterElevator(personId));
    }

    /**
     * Doors are opened and then closed because of the user not entering the elevator.
     */
    @Test
    public void openAndCloseDoors() throws Exception {
        PersonId personId = new PersonId(UUID.randomUUID());
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.registerResource(elevatorQueryHandler(elevatorId));

        fixture.givenAPublished(new PersonArrived(personId, 0, 2))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 1))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 0))
                .andThenAPublished(new ElevatorDoorsOpened(elevatorId))
                .whenPublishingA(new ElevatorDoorsClosed(elevatorId))
                .expectActiveSagas(0)
                .expectNoDispatchedCommands();
    }

    @Test
    public void closeDoors() throws Exception {
        PersonId personId = new PersonId(UUID.randomUUID());
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.registerResource(elevatorQueryHandler(elevatorId));

        fixture.givenAPublished(new PersonArrived(personId, 0, 2))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 1))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 0))
                .andThenAPublished(new ElevatorDoorsOpened(elevatorId))
                .whenPublishingA(new PersonEnteredElevator(personId))
                .expectActiveSagas(1)
                .expectDispatchedCommands(new CloseDoors(elevatorId));
    }

    @Test
    public void moveElevatorWithPerson() throws Exception {
        PersonId personId = new PersonId(UUID.randomUUID());
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.registerResource(elevatorQueryHandler(elevatorId));

        fixture.givenAPublished(new PersonArrived(personId, 0, 2))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 1))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 0))
                .andThenAPublished(new ElevatorDoorsOpened(elevatorId))
                .andThenAPublished(new PersonEnteredElevator(personId))
                .andThenAPublished(new ElevatorDoorsClosed(elevatorId))
                .whenPublishingA(new ElevatorMovedToFloor(elevatorId, 1))
                .expectActiveSagas(1)
                .expectNoDispatchedCommands();
    }

    @Test
    public void elevatorArrivesWithPerson() throws Exception {
        PersonId personId = new PersonId(UUID.randomUUID());
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.registerResource(elevatorQueryHandler(elevatorId));

        fixture.givenAPublished(new PersonArrived(personId, 0, 2))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 1))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 0))
                .andThenAPublished(new ElevatorDoorsOpened(elevatorId))
                .andThenAPublished(new PersonEnteredElevator(personId))
                .andThenAPublished(new ElevatorDoorsClosed(elevatorId))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 1))
                .whenPublishingA(new ElevatorMovedToFloor(elevatorId, 2))
                .expectActiveSagas(1)
                .expectDispatchedCommands(new OpenDoors(elevatorId));
    }

    @Test
    public void personLeaves() throws Exception {
        PersonId personId = new PersonId(UUID.randomUUID());
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.registerResource(elevatorQueryHandler(elevatorId));

        fixture.givenAPublished(new PersonArrived(personId, 0, 2))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 1))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 0))
                .andThenAPublished(new ElevatorDoorsOpened(elevatorId))
                .andThenAPublished(new PersonEnteredElevator(personId))
                .andThenAPublished(new ElevatorDoorsClosed(elevatorId))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 1))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 2))
                .whenPublishingA(new ElevatorDoorsOpened(elevatorId))
                .expectActiveSagas(1)
                .expectDispatchedCommands(new LeaveElevator(personId));
    }

    @Test
    public void emptyElevatorDoorsClose() throws Exception {
        PersonId personId = new PersonId(UUID.randomUUID());
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.registerResource(elevatorQueryHandler(elevatorId));

        fixture.givenAPublished(new PersonArrived(personId, 0, 2))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 1))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 0))
                .andThenAPublished(new ElevatorDoorsOpened(elevatorId))
                .andThenAPublished(new PersonEnteredElevator(personId))
                .andThenAPublished(new ElevatorDoorsClosed(elevatorId))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 1))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 2))
                .andThenAPublished(new ElevatorDoorsOpened(elevatorId))
                .whenPublishingA(new PersonLeftElevator(personId))
                .expectActiveSagas(1)
                .expectDispatchedCommands(new CloseDoors(elevatorId));
    }

    @Test
    public void emptyElevatorDone() throws Exception {
        PersonId personId = new PersonId(UUID.randomUUID());
        ElevatorId elevatorId = new ElevatorId(UUID.randomUUID());
        fixture.registerResource(elevatorQueryHandler(elevatorId));

        fixture.givenAPublished(new PersonArrived(personId, 0, 2))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 1))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 0))
                .andThenAPublished(new ElevatorDoorsOpened(elevatorId))
                .andThenAPublished(new PersonEnteredElevator(personId))
                .andThenAPublished(new ElevatorDoorsClosed(elevatorId))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 1))
                .andThenAPublished(new ElevatorMovedToFloor(elevatorId, 2))
                .andThenAPublished(new ElevatorDoorsOpened(elevatorId))
                .andThenAPublished(new PersonLeftElevator(personId))
                .whenPublishingA(new ElevatorDoorsClosed(elevatorId))
                .expectActiveSagas(0)
                .expectNoDispatchedCommands();
    }


}
