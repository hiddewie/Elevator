package com.hiddewieringa.elevator.domain.elevator.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class ElevatorId extends Identity<UUID> implements Serializable {
    public ElevatorId(UUID id) {
        super(id);
    }
}
