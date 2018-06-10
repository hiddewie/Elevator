package com.hiddewieringa.elevator.domain.elevator.event;

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class ElevatorCreated {

    @TargetAggregateIdentifier
    private final ElevatorId elevatorId;

    public ElevatorCreated(ElevatorId elevatorId) {
        this.elevatorId = elevatorId;
    }

    public ElevatorId getElevatorId() {
        return elevatorId;
    }
}
