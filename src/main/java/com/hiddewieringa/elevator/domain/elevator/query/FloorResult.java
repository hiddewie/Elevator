package com.hiddewieringa.elevator.domain.elevator.query;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class FloorResult {

    private final long floor;

    public FloorResult(long floor) {
        this.floor = floor;
    }

    public long getFloor() {
        return floor;
    }
}
