package com.hiddewieringa.elevator.domain.elevator.command;

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class CreateElevator {

    @TargetAggregateIdentifier
    private final ElevatorId elevatorId;

    public CreateElevator(ElevatorId elevatorId) {
        this.elevatorId = elevatorId;
    }

    public ElevatorId getElevatorId() {
        return elevatorId;
    }
}
