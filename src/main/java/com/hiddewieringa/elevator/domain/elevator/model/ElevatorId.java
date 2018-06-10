package com.hiddewieringa.elevator.domain.elevator.model;

import java.util.UUID;

public class ElevatorId extends Identity<UUID> {
    public ElevatorId(UUID id) {
        super(id);
    }
}
