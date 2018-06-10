package com.hiddewieringa.elevator.domain.elevator.command;

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class CallElevator {

    @TargetAggregateIdentifier
    private final ElevatorId elevatorId;
    private final long floor;

    public CallElevator(ElevatorId elevatorId, long floor) {
        this.elevatorId = elevatorId;
        this.floor = floor;
    }

    public ElevatorId getElevatorId() {
        return elevatorId;
    }

    public long getFloor() {
        return floor;
    }
}
