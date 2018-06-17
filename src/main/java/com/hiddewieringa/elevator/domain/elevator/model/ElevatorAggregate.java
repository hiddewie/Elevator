package com.hiddewieringa.elevator.domain.elevator.model;

import com.hiddewieringa.elevator.domain.elevator.command.CallElevator;
import com.hiddewieringa.elevator.domain.elevator.command.CreateElevator;
import com.hiddewieringa.elevator.domain.elevator.event.ElevatorCalled;
import com.hiddewieringa.elevator.domain.elevator.event.ElevatorCreated;
import com.hiddewieringa.elevator.domain.elevator.query.FloorResult;
import com.hiddewieringa.elevator.domain.elevator.query.QueryFloor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryMessage;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class ElevatorAggregate {

    @AggregateIdentifier
    private ElevatorId id;

    private long floor = 0L;

    private ElevatorAggregate() {
    }

    @CommandHandler
    public ElevatorAggregate(CreateElevator command) {
        apply(new ElevatorCreated(command.getElevatorId()));
    }

    @EventHandler
    public void on(ElevatorCreated event) {
        this.id = event.getElevatorId();
    }

    @CommandHandler
    public void call(CallElevator command) {
        apply(new ElevatorCalled(command.getElevatorId(), command.getFloor()));
    }

    @EventHandler
    public void elevatorCalled(ElevatorCalled event) {
        this.floor = event.getFloor();
    }

    @QueryHandler(queryName = "test")
    public FloorResult queryFloor(QueryMessage<QueryFloor, FloorResult> queryMessage) {
//        org.axonframework.commandhandling.model.Aggregate<ElevatorAggregate> q= elevatorAggregateRepository.load(query.getElevatorId().toString());
        return new FloorResult(floor);
    }

    @QueryHandler(queryName = "test")
    public FloorResult queryFloor2(QueryFloor queryFloor) {
//        org.axonframework.commandhandling.model.Aggregate<ElevatorAggregate> q= elevatorAggregateRepository.load(query.getElevatorId().toString());
        return new FloorResult(floor);
    }

    public long getFloor() {
        return floor;
    }
}
