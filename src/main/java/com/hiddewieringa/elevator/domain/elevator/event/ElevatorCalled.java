package com.hiddewieringa.elevator.domain.elevator.event;

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class ElevatorCalled {

    @TargetAggregateIdentifier
    private final ElevatorId elevatorId;
    private final long floor;

    public ElevatorCalled(ElevatorId elevatorId, long floor) {
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
