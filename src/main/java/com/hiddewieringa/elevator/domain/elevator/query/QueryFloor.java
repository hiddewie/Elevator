package com.hiddewieringa.elevator.domain.elevator.query;

import com.hiddewieringa.elevator.domain.elevator.model.ElevatorId;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class QueryFloor {

    @TargetAggregateIdentifier
    private final ElevatorId elevatorId;

    public QueryFloor(ElevatorId elevatorId) {
        this.elevatorId = elevatorId;
    }

    public ElevatorId getElevatorId() {
        return elevatorId;
    }

}
